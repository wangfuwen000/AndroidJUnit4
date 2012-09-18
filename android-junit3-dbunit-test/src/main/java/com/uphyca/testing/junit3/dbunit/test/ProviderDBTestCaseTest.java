package com.uphyca.testing.junit3.dbunit.test;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;

import android.content.ContentValues;
import android.content.Context;

import com.uphyca.testing.junit3.dbunit.ProviderDBTestCase;

public class ProviderDBTestCaseTest extends ProviderDBTestCase<TinyContentProvider> {

    public ProviderDBTestCaseTest() {
        super(TinyContentProvider.class, TinyContentProvider.AUTHORITY);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return getFlatXmlDataSetFromRawResrouce(R.raw.provider_db_empty);
    }

    @Override
    protected String getDatabaseName() {
        return TinySQLiteOpenHelper.DATABASE_NAME;
    }

    @Override
    protected void onCreateDatabase(Context context) {
        //Call SQLiteOpenHelper#getWritableDatabase() for create database.
        new TinySQLiteOpenHelper(context).getWritableDatabase().close();
    }

    public void testPreconditions() {
        assertNotNull(getProvider());
    }

    public void testInsert() throws Exception {

        ContentValues values = new ContentValues();
        values.put("name",
                   "foo");
        getProvider().insert(TinyContentProvider.CONTENT_URI,
                             values);

        // Fetch database data after executing your code
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("tiny");

        // Load expected data from an XML dataset
        IDataSet expectedDataSet = getFlatXmlDataSetFromRawResrouce(R.raw.provider_db_after_insert);
        ITable expectedTable = expectedDataSet.getTable("tiny");

        // Assert actual database table match expected table
        Assertion.assertEquals(expectedTable,
                               actualTable);
    }

    public void testDelete() throws Exception {

        DatabaseOperation.CLEAN_INSERT.execute(getConnection(),
                                               getFlatXmlDataSetFromRawResrouce(R.raw.provider_db_after_insert));

        getProvider().delete(TinyContentProvider.CONTENT_URI.buildUpon().appendPath("1").build(),
                             null,
                             null);

        // Fetch database data after executing your code
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("tiny");

        // Load expected data from an XML dataset
        IDataSet expectedDataSet = getFlatXmlDataSetFromRawResrouce(R.raw.provider_db_empty);
        ITable expectedTable = expectedDataSet.getTable("tiny");

        // Assert actual database table match expected table
        Assertion.assertEquals(expectedTable,
                               actualTable);
    }

    public void testUpdate() throws Exception {

        DatabaseOperation.CLEAN_INSERT.execute(getConnection(),
                                               getFlatXmlDataSetFromRawResrouce(R.raw.provider_db_after_insert));

        IDataSet assertDataSet = getConnection().createDataSet();
        ITable assertTable = assertDataSet.getTable("tiny");
        assertEquals(1,
                     assertTable.getRowCount());

        ContentValues values = new ContentValues();
        values.put("name",
                   "buz");
        getProvider().update(TinyContentProvider.CONTENT_URI.buildUpon().appendPath("1").build(),
                             values,
                             null,
                             null);

        // Fetch database data after executing your code
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("tiny");

        // Load expected data from an XML dataset
        IDataSet expectedDataSet = getFlatXmlDataSetFromRawResrouce(R.raw.provider_db_after_update);
        ITable expectedTable = expectedDataSet.getTable("tiny");

        // Assert actual database table match expected table
        Assertion.assertEquals(expectedTable,
                               actualTable);
    }

}
