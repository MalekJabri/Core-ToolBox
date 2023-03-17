package com.ibm.mj.core.sandbox;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.property.PropertyImpl;
import com.ibm.mj.core.sandbox.tester.testerClass;
import filenet.vw.base.logging.Logger;

import java.util.Iterator;

public class TestConnection extends testerClass{

	public static final String  documentID = "{C0D50D83-0000-CF13-AC39-DB662671129D}";
	public static final String COUNTER_CLASS="CounterObject";
	public static final String COUNTER_PREFIX="CounterPrefix";
	public static final String COUNTER_ATTRIBUTE_TARGET="CounterAttribute";
	public static final String COUNTER_NAME="CounterName";
	public static final String COUNTER_NEXT_ID_ATTRIBUTE="Count";


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestConnection ts = new TestConnection();
		System.out.println(ts.getOs().get_SymbolicName());
		Document test= Factory.Document.fetchInstance(ts.os, new Id(documentID),null);
		Iterator properties = test.getProperties().iterator();
		System.out.println("t"+ test.getProperties().getInteger32Value("Count"));

		Logger logger = Logger.getLogger(TestConnection.class);
		while(properties.hasNext()){
			PropertyImpl prop = (PropertyImpl) properties.next();
			logger.info(prop.getClass().getSimpleName());

		}
		//ts.getAction();
	}


	@Override
	public void getAction() {

		Document doc = Factory.Document.fetchInstance(os, documentID, null);
		doc.getObjectStore();
		CustomObject dispenser = null;
		FilterElement counterProperty1 =	new FilterElement(null,null,null, "CounterAttribute",null);
		FilterElement counterProperty2 =	new FilterElement(null,null,null,"CounterPrefix",null);
		FilterElement counterProperty3 =	new FilterElement(null,null,null,"Count",null);
		PropertyFilter propertyFilter =  new PropertyFilter();
		propertyFilter.addIncludeProperty(counterProperty1);
		propertyFilter.addIncludeProperty(counterProperty2);
		propertyFilter.addIncludeProperty(counterProperty3);
		String className = doc.getClassName();
		String AttributeToUpdate = null;
		String prefix  = null;
		int compt = 0;
		dispenser = Factory.CustomObject.fetchInstance(os, "/config/"+className, propertyFilter);
		if(dispenser == null ) System.out.println("Not Found");
		else {
			if(dispenser.isLocked())
				System.out.println("Init is locked");
			else System.out.println("not locked");
			Properties dispenserProperties = dispenser.getProperties();
			 AttributeToUpdate =  dispenserProperties.getStringValue("CounterAttribute");
			 prefix = dispenserProperties.getStringValue("CounterPrefix");
			for(int i =0;i<50;i++) {
				try {
					if(dispenser.isLocked()) {
						dispenser= Factory.CustomObject.fetchInstance(os, "/config/"+className, propertyFilter);
						System.out.println("dispenser is locked ");
					}
					else{
						System.out.println("dispenser is current updated ");
						dispenser.lock(10, null);
						System.out.println("dispenser is  locked ");
						dispenser.save(RefreshMode.REFRESH);
						dispenser.fetchProperties(propertyFilter);
						dispenserProperties = dispenser.getProperties();
						try {
							compt  = dispenserProperties.getInteger32Value("Count");
						}catch(Exception e ) {
							System.out.println("the id is null set 0");

						}
						compt++;

						dispenserProperties.putValue("Count",compt);
						dispenser.unlock();
						dispenser.save(RefreshMode.REFRESH);
						System.out.println("dispenser  has been updated ");
						break;
					}
				}catch (EngineRuntimeException e ) {
					try {
						dispenser = Factory.CustomObject.fetchInstance(os, "/config/"+className, null);
						Thread.sleep(5);
					} catch (InterruptedException e1) {
						System.out.println("Cant save the system");
					}
				}
			}
		}
		if(compt>0 && AttributeToUpdate != null){
			if(doc.getProperties().get(AttributeToUpdate).getClass().getSimpleName().contains("Integer")) {
				doc.getProperties().putValue(AttributeToUpdate, compt);
			}else {
				if(prefix!=null ) doc.getProperties().putValue(AttributeToUpdate, prefix+compt);
				else  doc.getProperties().putValue(AttributeToUpdate, compt+"");
			}
			System.out.println(dispenser.getProperties().get("Count").getInteger32Value() + " is the current count");
			doc.save(RefreshMode.NO_REFRESH);
		}

	}


}
