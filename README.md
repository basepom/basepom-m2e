# M2E support for basepom.org

If plugins are executed early (in the validate phase), Eclipse m2e
will fail to build these projects because it does not know how to deal
with the plugins before the compile phase.

This project contains m2e extensions for

- Mycila license plugin (http://code.mycila.com/license-maven-plugin/)
- Ning duplicate finder plugin (https://github.com/ning/maven-duplicate-finder-plugin)
- Ning dependency versions check plugin (https://github.com/ning/maven-dependency-versions-check-plugin)
- org.basepom duplicate finder plugin (https://github.com/basepom/duplicate-finder-maven-plugin)

which can be used in the "verify" stage. All m2e extensions can be
discovered through the m2e catalog and installed in Eclipse to get rid
of the build errors.

These m2e extensions only disable the plugins for Eclipse.

- org.basepom Property Helper plugin (https://github.com/basepom/property-helper-maven-plugin)

which can be used in any stage but is most useful in "validate". The 'get' goal of this plugin will
be executed by Eclipse, the 'inc' goal is ignored .
