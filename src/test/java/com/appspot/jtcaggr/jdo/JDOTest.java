package com.appspot.jtcaggr.jdo;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocal;
import com.google.appengine.tools.development.ApiProxyLocalFactory;
import com.google.appengine.tools.development.LocalServerEnvironment;
import com.google.apphosting.api.ApiProxy;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shyiko
 * @since Aug 7, 2010
 */
public abstract class JDOTest implements IHookable {

    private static final ApiProxy.Environment environment = new ApiProxy.Environment() {

        private Map<String, Object> attributes = new HashMap<String, Object>();

        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }

        @Override
        public String getAppId() {
            return "Unit Tests";
        }

        @Override
        public String getVersionId() {
            return "1.0";
        }

        @Override
        public String getRequestNamespace() {
            return "";
        }

        @Override
        public String getAuthDomain() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isLoggedIn() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getEmail() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isAdmin() {
            throw new UnsupportedOperationException();
        }
    };

    private static final LocalServerEnvironment localServerEnvironment = new LocalServerEnvironment() {
            public File getAppDir() {
                return new File(".");
            }

            public String getAddress() {
                return null;
            }

            public int getPort() {
                return 0;
            }

            public void waitForServerToStart() throws InterruptedException {
            }
        };

    private static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("testpmf");

    @Override
    public void run(IHookCallBack iHookCallBack, ITestResult iTestResult) {
        boolean failed = false;
        try {
            iHookCallBack.runTestMethod(iTestResult);
        } catch (RuntimeException ex) {
            failed = true;
            throw ex;
        } finally {
            Transaction txn = DatastoreServiceFactory.getDatastoreService().getCurrentTransaction(null);
            if (txn != null) {
                try {
                    txn.rollback();
                } finally {
                    // making sure that the test, which was successful so far,
                    // didn't leave any resources in an inconsistent state
                    if (!failed) {
                        throw new IllegalStateException(
                                "Datastore service still has an active transaction. " +
                                "Please rollback or commit all transactions before test completes.");
                    }
                }
            }
        }
    }

    @BeforeMethod(alwaysRun = true)
    protected void setUpAppEngineEnvironment() throws Exception {
        ApiProxyLocal proxy = new ApiProxyLocalFactory().create(localServerEnvironment);
        proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
        ApiProxy.setDelegate(proxy);
        ApiProxy.setEnvironmentForCurrentThread(environment);
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDownAppEngineEnvironment() throws Exception {
        ApiProxy.clearEnvironmentForCurrentThread();
    }

    public PersistenceManagerFactory getPersistenceManagerFactory() {
        return pmf;
    }

    public PersistenceManager getNewPersistenceManager() {
        return pmf.getPersistenceManager();
    }
}
