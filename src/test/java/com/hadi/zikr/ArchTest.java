package com.hadi.zikr;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.hadi.zikr");

        noClasses()
            .that()
            .resideInAnyPackage("com.hadi.zikr.service..")
            .or()
            .resideInAnyPackage("com.hadi.zikr.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.hadi.zikr.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
