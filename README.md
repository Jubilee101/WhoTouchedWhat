### Who Touched What?


This a git analysis tool for you to see how many edits were done to each file and by whom. A quick and straight forward way to track work division and citizenship. Start up the server and input your local git repository directory, wait, and voila~~

<br/>

![Home page](https://github.com/Jubilee101/WhoTouchedWhat/blob/main/img/demo.png)

<br />

![Show case](https://github.com/Jubilee101/WhoTouchedWhat/blob/main/img/demo.gif)

<br />

#### Features

1. Quick parsing, estimated time for going through all files with 10k+ commits is under 10 minutes. 
2. Result caching, parsed result will be stored as committer_log.json under given repository directory
3. Ignore file name/directory changes, using JGit, Who Touched What can perform operations similar to `git log --follow`, ignoring directory changes

<br />
This part is the back-end implementation of Who Touched What?.

See front-end at [WhoTouchedWhatFE](https://github.com/Jubilee101/WhoTouchedWhatFE)

#### Notice
Since Who Touched What parses the file tree based on the last commit, if some file is
no longer in the directory tree, its line changes WILL NOT be added up all the way to root directory,
causing total line changes corresponding to each contributor not the same as what GitHub tells you. 
In short, it doesn't see forgotten contributions. However, it still sees all the changes in existing files
despite any name or directory changes.

