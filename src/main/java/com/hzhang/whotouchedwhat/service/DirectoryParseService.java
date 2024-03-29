package com.hzhang.whotouchedwhat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzhang.whotouchedwhat.model.Directory;
import com.hzhang.whotouchedwhat.utils.ColorGenerator;
import com.hzhang.whotouchedwhat.utils.GetFileHistory;
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
    private String address;
    public DirectoryParseService() {
        root = new Directory(0);
    }
    // if root has no authors, that means this is the first time
    // we need to color it, sum up the authors and save the result
    public Directory getRoot() {
        if (!root.hasAuthors()) {
            root.getAllChanges();
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(Paths.get(UriEncoder.decode(address), "committer_info.json").toFile(), root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return root;
    }

    public void parseDirectory(String address){
        String newAddress = Paths.get(UriEncoder.decode(address), ".git").toString();
        this.address = address;
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            File file = new File(Paths.get(UriEncoder.decode(address), "committer_info.json").toString());
            if (file.exists() && !file.isDirectory()) {
                ObjectMapper mapper = new ObjectMapper();
                root = mapper.readValue(file, Directory.class);
                return;
            }
            repository = builder.setGitDir(new File(newAddress))
                    .readEnvironment()
                    .findGitDir()
                    .build();

            Ref head = repository.findRef("HEAD");
            root.setName(head.getName());
            root.setPath("");
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
            if (repository != null) {
                repository.close();
            }

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
            if (treeWalk.getPathString().equals(node.getPath())) {
                node.getAllChanges();
                return;
            }
            Directory file = new Directory(0);
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
                GetFileHistory fileHistoryService = new GetFileHistory();
                fileHistoryService.getAuthorHistory(file.getPath(),
                        repository.getDirectory().getAbsolutePath());
                file.setAuthors(fileHistoryService.getHistory());
            }
        }
    }
}
