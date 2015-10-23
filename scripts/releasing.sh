#! /bin/sh

export RELEASE=$1
export SNAPSHOT=$2
find . -name MANIFEST.MF -o -name feature.xml | xargs perl -pi -e "s|${RELEASE}.qualifier|${RELEASE}|"
git add -u
git commit -m "set osgi versions to ${RELEASE} for release"
mvn clean release:clean release:prepare release:perform
git push
git push --tags
find . -name MANIFEST.MF -o -name feature.xml | xargs perl -pi -e "s|${RELEASE}|${SNAPSHOT}.qualifier|"
git add -u
git commit -m "set osgi versions to ${SNAPSHOT}.qualifier for development"
