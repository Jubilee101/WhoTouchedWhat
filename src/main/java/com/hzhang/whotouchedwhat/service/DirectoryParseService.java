package com.hzhang.whotouchedwhat.service;

import com.hzhang.whotouchedwhat.model.Directory;
import exceptions.InvalidDirectoryException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class DirectoryParseService {
    private Directory root;
    public DirectoryParseService() {
        root = new Directory();
    }

    public Directory getRoot() {
        return root;
    }

    public void parseDirectory(String address){
        address = Paths.get(address + "\\.git").toString();
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            Repository repository = builder.setGitDir(new File(address))
                    .readEnvironment()
                    .findGitDir()
                    .build();
            Ref head = repository.findRef("HEAD");
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
                return;
            }
            Directory file = new Directory();
            file.setName(treeWalk.getNameString());
            node.addDirectory(file);
            if (treeWalk.isSubtree()) {
                treeWalk.enterSubtree();
                buildDirectoryTree(treeWalk, file);
            }
        }
    }
}
