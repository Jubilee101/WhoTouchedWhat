package com.hzhang.whotouchedwhat.service;

import com.hzhang.whotouchedwhat.model.AuthorHistory;
import com.hzhang.whotouchedwhat.model.Directory;
import com.hzhang.whotouchedwhat.utils.LogFollowCommand;
import exceptions.InvalidDirectoryException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class DirectoryParseService {
    private Directory root;
    private Repository repository;
    public DirectoryParseService() {
        root = new Directory();
    }

    public Directory getRoot() {
        return root;
    }

    public void parseDirectory(String address){
        address = UriEncoder.decode(address);
        address = Paths.get(address, ".git").toString();
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            repository = builder.setGitDir(new File(address))
                    .readEnvironment()
                    .findGitDir()
                    .build();

            Ref head = repository.findRef("HEAD");
            root.setName(head.getName());
            RevWalk walk = new RevWalk(repository);
            RevCommit commit = walk.parseCommit(head.getObjectId());
            RevTree tree = commit.getTree();
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(false);
            treeWalk.setPostOrderTraversal(true);
            buildDirectoryTree(treeWalk, root);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidDirectoryException("Unable to parse directory");
        } finally {
            repository.close();
        }
    }

    /**
     * do a dfs of current directory, build the directory recursively
     * @param treeWalk
     * @param node
     * @throws IOException
     */
    private void buildDirectoryTree(TreeWalk treeWalk, Directory node) throws IOException {
        while (treeWalk.next()) {
            /**
             * Since we set post order traversal to be true,
             * when all the current subtree's children have been traversed,
             * treewalk will return this subtree's root again, then we shall know that
             * this subtree has been traversed and return to upper layer
             */
            if (treeWalk.getNameString().equals(node.getName())) {
                node.getAllChanges();
                return;
            }
            Directory file = new Directory();
            file.setName(treeWalk.getNameString());
            file.setPath(treeWalk.getPathString());
            node.addDirectory(file);
            boolean isLeaf = true;
            if (treeWalk.isSubtree()) {
                isLeaf = false;
                treeWalk.enterSubtree();
                buildDirectoryTree(treeWalk, file);
            }
            if (isLeaf) {
                GetFileHistoryService fileHistoryService = new GetFileHistoryService();
                fileHistoryService.getAuthorHistory(file.getPath(),
                        repository.getDirectory().getAbsolutePath());
                file.setAuthors(fileHistoryService.getHistory());
            }
        }
    }
}
