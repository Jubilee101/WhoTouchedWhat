package com.hzhang.whotouchedwhat.service;

import com.hzhang.whotouchedwhat.model.AuthorHistory;
import com.hzhang.whotouchedwhat.utils.LogFollowCommand;
import exceptions.InvalidDirectoryException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class GetFileHistoryService {
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
            for (RevCommit commit : rw) {
                history.addContributor(commit.getAuthorIdent().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidDirectoryException("Unable to parse directory");
        } finally {
            repository.close();
        }
    }

    public AuthorHistory getHistory(){
        return this.history;
    }
}
