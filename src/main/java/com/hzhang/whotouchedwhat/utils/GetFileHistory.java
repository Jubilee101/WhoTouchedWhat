package com.hzhang.whotouchedwhat.utils;

import com.hzhang.whotouchedwhat.model.Author;
import com.hzhang.whotouchedwhat.model.AuthorHistory;
import com.hzhang.whotouchedwhat.utils.LogFollowCommand;
import exceptions.InvalidDirectoryException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetFileHistory {
    private Repository repository;
    private AuthorHistory history;

    public void getAuthorHistory(String filePath, String repoAddress) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        repoAddress = UriEncoder.decode(repoAddress);
        history = new AuthorHistory();
        try {
            repository = builder.setGitDir(new File(repoAddress))
                    .readEnvironment()
                    .findGitDir()
                    .build();
            RevWalk rw = LogFollowCommand.follow(repository, filePath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter df = new DiffFormatter(out);
            Set<String> paths = new HashSet<>();
            paths.add(filePath);
            List<RevCommit> commits = new ArrayList<>();
            for (RevCommit commit : rw) {
                commits.add(commit);
            }
            for (int i = 0; i < commits.size(); i++) {
                RevCommit commit = commits.get(i);
                df.setRepository(repository);
                df.setDiffComparator(RawTextComparator.DEFAULT);
                df.setDetectRenames(true);
                List<DiffEntry> diffs;
                if (commit.getParentCount() == 0) {
                    AbstractTreeIterator oldTreeIter = new EmptyTreeIterator();
                    ObjectReader reader = repository.newObjectReader();
                    AbstractTreeIterator newTreeIter = new CanonicalTreeParser(null, reader, commit.getTree());
                    diffs = df.scan(oldTreeIter, newTreeIter);
                }
                else {
                    diffs = df.scan(commit.getParent(0).getTree(),commit.getTree());
                }
                for (DiffEntry diff : diffs) {
                    String newPath = diff.getNewPath();
                    String oldPath = diff.getOldPath();
                    int time = commit.getCommitTime();
//                    System.out.println("time " + time);
//                    System.out.println("new " + newPath);
//                    System.out.println("old " + oldPath);

                    if (paths.contains(newPath)) {
                        if (!oldPath.equals("/dev/null")){
                            paths.add(oldPath);
                        }
                    } else if (newPath.equals("/dev/null") && paths.contains(oldPath)) {
                        // do nothing
                    }
                    else {
                        continue;
                    }
                    int count = 0;
                    for (Edit edit : df.toFileHeader(diff).toEditList()) {
                        count += edit.getEndA() - edit.getBeginA();
                        count += edit.getEndB() - edit.getBeginB();
                    }
                    if (count == 0) {
                        count = 1;
                    }
                    history.addContributor(commit.getAuthorIdent().getName(), count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidDirectoryException("Unable to parse directory");
        } finally {
            repository.close();
        }
    }

    public List<Author> getHistory(){
        this.history.convertMapToList();
        return this.history.getAuthors();
    }
}
