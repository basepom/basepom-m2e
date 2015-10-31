# How to release the update site

- update the OSGi version to match the upcoming release version

```bash
% export RELEASE=<new release version>;  find . -name MANIFEST.MF -o -name feature.xml | xargs perl -pi -e "s|${RELEASE}.qualifier|${RELEASE}|"
% git add -u
% git commit -m "set osgi versions to ${RELEASE} for release"
```

- do the maven release dance

```bash
% mvn clean release:clean release:prepare release:perform
% git push
% git push --tags
```

Close and release the oss release repository.

- update the OSGi version to match the new development version

```bash
% export RELEASE=<release version> SNAPSHOT=<snapshot version>;  find . -name MANIFEST.MF -o -name feature.xml | xargs perl -pi -e "s|${RELEASE}|${SNAPSHOT}.qualifier|"
% git add -u
% git commit -m "set osgi versions to ${SNAPSHOT}.qualifier for development"
```

- update the update site

```bash
% export RELEASE=<release version>
% git checkout gh-pages
% cd update-site
% mkdir $RELEASE
% cd $RELEASE
% unzip ../target/update-site-$RELEASE.zip
% cd ..
% rm -rf latest ; cp -av $RELEASE latest
% git add $RELEASE latest
% git commit -m "add $RELEASE site"
% git push
```

