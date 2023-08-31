package com.ibm.mj.core.sandbox;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CmAbstractPersistable;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Property;
import com.filenet.api.util.Id;
import com.ibm.mj.core.ceObject.DocumentTool;
import com.ibm.mj.core.sandbox.tester.testerClass;

public class TestSalesforce extends testerClass {

    static String SF_OBJ_ID = "006Dn000005vk3XIAQ";
    static String SF_ORG_ID = "00DDn00000DCwsDMAT";
    static String SF_OBJ_TYPE = "Opportunity";
    static String SF_OBJ_URL = "https://emea-10-dev.my.salesforce.com/lightning/r/Opportunity/006Dn000005vk3XIAQ/view";

    static String sfDocument = "{904B408A-0000-CF17-B846-44E38E1537FB}";


    TestSalesforce(String config){
        setConfig(config);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        TestSalesforce ts = new TestSalesforce("/Users/jabrimalek/development/tools/FileNet_Config/config_cpe_demo10.properties");
        ts.getAction();
    }

    @Override
    public void getAction() {
        System.out.println("test connection: "+ getOs().get_Name());
        Document doc = DocumentTool.getDocument(getOs(), sfDocument);
        System.out.println(doc.get_Name());
        createSalesforceRelationship(getOs(), doc);

    }


    private void createSalesforceRelationship(ObjectStore os, Document doc)
    {
        CmAbstractPersistable obj = Factory.CmAbstractPersistable.createInstance(os, "SfSalesforceRelationship");
        obj.getProperties().putValue("SfSalesforceObjectId", SF_OBJ_ID);
        obj.getProperties().putValue("SfSalesforceOrganizationId", SF_ORG_ID);
        obj.getProperties().putValue("SfSalesforceObjectType", SF_OBJ_TYPE);
        obj.getProperties().putValue("SfSalesforceObjectUrl", SF_OBJ_URL);
        obj.getProperties().putObjectValue("SfDocument", doc);
        Property vsProp = doc.fetchProperty("VersionSeries", null);
        Id vsId = vsProp.getIdValue();
        obj.getProperties().putValue("SfVersionSeriesId", vsId);
        obj.save(RefreshMode.NO_REFRESH);
        System.out.println("Added Salesforce relationship, VersionSeriesId = " + vsId);
    }
}
