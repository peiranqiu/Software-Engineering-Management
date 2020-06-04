# Messaging Application

Team 4, CS5500, summer I 2020

Collaborators: [@daniehao](https://github.ccs.neu.edu/daniehao)  [@yinrouni](https://github.ccs.neu.edu/yinrouni)  [@qjgong](https://github.ccs.neu.edu/qjgong)  [@peggypeggyq](https://github.ccs.neu.edu/peggypeggyq)

---

This repository is for CS 5500 Foundation of Software Engineering, Summer I 2020, team project. Below is our current development guideline.

## Branching

1. `master` — this is the source code; code on this branch should be clean, well-documented, fully tested and bug-free. `develop` will be the *working* version of this branch.
2. `develop` — this is the *working* branch of `master`. Testing purposes only and to ensure individual code is cohesive and conflict-free. Once you are done with your individual branches, submit a PR for review for merging. All merge conflicts are your responsibility and should be addressed here.

## Committing

To create a new branch, checkout `develop` and run:

```
git pull
```

To get the most up to date version. Initiate all branch names with `issue-#` where `#` is the number of your issue, i.e. `issue-01-local-set-up`. The full command is:

```
git checkout -b issue-#-some-descriptive-text
```

Commits should be descriptive of the work that has been done, as a rule of thumb a commit would look like this:

```
git commit -m "[issue-#] short description of the work"
```

As you work through your issue and feel that you're done and ready to integrate:

```
git push -u origin your_branch
```

Open a PR to `develop` and select team members to request a code review. 

## Merging

- It is recommended to `squash and merge` instead of `create a merge commit`. This will keep the commit history clean/concise when pushing features into develop. Try to write a useful commit message for the merge commit so we know what the commit addresses and why.

- Delete the source branch once merged.

  

For additional information on the project, check out **[Project's Wiki](https://github.ccs.neu.edu/cs5500-fse/team-4-su20/wiki)**.
