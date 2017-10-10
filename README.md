# project-adele
[![Build Status of project-adele](https://travis-ci.org/microservicesteam/project-adele.svg?branch=master)](https://travis-ci.org/microservicesteam/project-adele) [![codecov](https://codecov.io/gh/microservicesteam/project-adele/branch/master/graph/badge.svg)](https://codecov.io/gh/microservicesteam/project-adele)

Project Adele

### How to release a version?

```
./release.sh release
```

This command will:
1. Build the whole project with Maven
2. Set the version based on the current `SNAPSHOT` version
3. Create a new tag as `adele-<DATE>` in git
4. Roll the `SNAPSHOT` version to the next day

As a result of the new tag a special Travis build will kick in, which generates the release artifacts and the proper Docker image.

### How to set the version explicitly?

```
./release version [newVersion]
```

This command will instruct Maven to set a new `SNAPHSHOT` version without doing any release activity. These changes needs to be committed and pushed manually.
