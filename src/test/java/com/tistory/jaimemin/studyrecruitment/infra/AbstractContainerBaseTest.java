package com.tistory.jaimemin.studyrecruitment.infra;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author jaime
 * @title AbstractContainerBaseTest
 * @see\n <pre>
 * </pre>
 * @since 2022-05-08
 */
public abstract class AbstractContainerBaseTest {

    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer();
        POSTGRE_SQL_CONTAINER.start();
    }
}
