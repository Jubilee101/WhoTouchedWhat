package com.hzhang.whotouchedwhat.utils;

import org.eclipse.jgit.diff.DiffConfig;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.FollowFilter;
import org.eclipse.jgit.revwalk.RenameCallback;
import org.eclipse.jgit.revwalk.RevWalk;

import java.util.ArrayList;
import java.util.List;

public class LogFollowCommand {

    private static class DiffCollector extends RenameCallback {
        List<DiffEntry> diffs = new ArrayList<>();

        @Override
        public void renamed(DiffEntry diff) {
            diffs.add(diff);
        }
    }

    public static RevWalk follow(Repository repository, String followPath) {
        try{
            Config config = repository.getConfig();
            config.setBoolean("diff", null, "renames", true);

            RevWalk rw = new RevWalk(repository);
            DiffCollector diffCollector = new DiffCollector();

            DiffConfig dc = config.get(DiffConfig.KEY);
            FollowFilter followFilter =
                    FollowFilter.create(followPath, dc);
            followFilter.setRenameCallback(diffCollector);
            rw.setTreeFilter(followFilter);
            rw.markStart(rw.parseCommit(repository.resolve(Constants.HEAD)));
            return rw;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
