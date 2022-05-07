package com.tistory.jaimemin.studyrecruitment;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * @author jaime
 * @title PackageDependencyTest
 * @see\n <pre>
 * </pre>
 * @since 2022-05-07
 */
@AnalyzeClasses(packagesOf = StudyRecruitmentApplication.class)
public class PackageDependencyTest {

    private static final String STUDY = "..modules.study..";

    private static final String EVENT = "..modules.event..";

    private static final String ACCOUNT = "..modules.account..";

    private static final String TAG = "..modules.tag..";

    private static final String ZONE = "..modules.zone..";

    private static final String MODULES_PACKAGE = "com.tistory.jaimemin.studyrecruitment.modules..";

    @ArchTest
    ArchRule modulesPackageRule = classes().that().resideInAPackage(MODULES_PACKAGE)
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .resideInAnyPackage(MODULES_PACKAGE);

    @ArchTest
    ArchRule studyPackageRule = classes().that().resideInAPackage(STUDY)
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .resideInAnyPackage(STUDY, EVENT);

    @ArchTest
    ArchRule eventpackageRule = classes().that().resideInAnyPackage(EVENT)
            .should()
            .accessClassesThat()
            .resideInAnyPackage(STUDY, ACCOUNT, EVENT);

    @ArchTest
    ArchRule accountPackageRule = classes().that().resideInAnyPackage(ACCOUNT)
            .should()
            .accessClassesThat()
            .resideInAnyPackage(TAG, ZONE, ACCOUNT);

    @ArchTest
    ArchRule cycleCheck = slices().matching("com.tistory.jaimemin.studyrecruitment.modules.(*)..")
            .should()
            .beFreeOfCycles();
}
