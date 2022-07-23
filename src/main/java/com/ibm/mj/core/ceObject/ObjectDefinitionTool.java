package com.ibm.mj.core.ceObject;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.LocalizedString;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyTemplateInteger32;
import com.filenet.api.admin.PropertyTemplateString;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.constants.Cardinality;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.PropertyFilter;

public class ObjectDefinitionTool {
	 public static void addStringPropertyDefinition(ObjectStore os,ClassDefinition objClassDef, String propertyName, String symbolicName, boolean isName){
		// Construct property filter to ensure PropertyDefinitions property of CD is returned as evaluated
		        PropertyFilter pf = new PropertyFilter();
		        pf.addIncludeType(0, null, Boolean.TRUE, FilteredPropertyType.ANY, null);


		// Create property template for a single-valued string property
		        String strPropTemplateName = propertyName;
		        PropertyTemplateString objPropTemplate = Factory.PropertyTemplateString.createInstance(os);

		// Set cardinality of properties that will be created from the property template
		        objPropTemplate.set_Cardinality (Cardinality.SINGLE);

		// Set up locale
		        LocalizedString locStr = Factory.LocalizedString.createInstance();
		        locStr.set_LocalizedText(strPropTemplateName);
		        locStr.set_LocaleName (os.get_LocaleName());

		// Create LocalizedString collection
		        objPropTemplate.set_DisplayNames (Factory.LocalizedString.createList());
		        objPropTemplate.get_DisplayNames().add(locStr);
		        objPropTemplate.set_IsNameProperty(isName);

		// Save new property template to the server
		        objPropTemplate.save(RefreshMode.REFRESH);

		// Create property definition from property template
		        PropertyDefinition objPropDef = objPropTemplate.createClassProperty();

		// Get PropertyDefinitions property from the property cache                     
		        PropertyDefinitionList objPropDefs = objClassDef.get_PropertyDefinitions();

		// Add new property definition to class definition
		        objPropDefs.add(objPropDef);
		        objClassDef.save(RefreshMode.REFRESH);
		        System.out.println("Add new property "+propertyName );
		    }

		    public static void addIntPropertyDefinition(ObjectStore os,ClassDefinition objClassDef, String propertyName, String symbolicName){

		// Create property template for a single-valued string property

		        String strPropTemplateName = propertyName;
		        PropertyTemplateInteger32 objPropTemplate = Factory.PropertyTemplateInteger32.createInstance(os);

		// Set cardinality of properties that will be created from the property template
		        objPropTemplate.set_Cardinality (Cardinality.SINGLE);

		// Set up locale
		        LocalizedString locStr = Factory.LocalizedString.createInstance();
		        locStr.set_LocalizedText(strPropTemplateName);
		        locStr.set_LocaleName (os.get_LocaleName());

		// Create LocalizedString collection
		        objPropTemplate.set_DisplayNames (Factory.LocalizedString.createList());
		        objPropTemplate.get_DisplayNames().add(locStr);
		        objPropTemplate.set_SymbolicName(symbolicName);

		// Save new property template to the server
		        objPropTemplate.save(RefreshMode.REFRESH);
		        objPropTemplate.set_PropertyDefaultInteger32(0);

		// Create property definition from property template
		        PropertyDefinition objPropDef = objPropTemplate.createClassProperty();

		// Get PropertyDefinitions property from the property cache
		        PropertyDefinitionList objPropDefs = objClassDef.get_PropertyDefinitions();

		// Add new property definition to class definition
		        objPropDefs.add(objPropDef);
		        objClassDef.save(RefreshMode.REFRESH);

		        System.out.println("Add new property "+propertyName );
		    }
		    
		    public static ClassDefinition CreateClassDefinition(ObjectStore os, String superClass, String className, String SymbolicName){
		        ClassDefinition objClassDef = Factory.ClassDefinition.fetchInstance(os, superClass, null);
		        ClassDefinition objClassDefNew = objClassDef.createSubclass();
		        // Set up locale
		        LocalizedString objLocStr = Factory.LocalizedString.createInstance();
		        objLocStr.set_LocalizedText(className);
		        objLocStr.set_LocaleName(os.get_LocaleName());
		        objClassDefNew.set_SymbolicName(SymbolicName);
		        // Create LocalizedStringList collection
		        objClassDefNew.set_DisplayNames(Factory.LocalizedString.createList());
		        objClassDefNew.get_DisplayNames().add(objLocStr);
		        // Save new class definition to the server
		        objClassDefNew.save(RefreshMode.REFRESH);
		        System.out.println("New class definition created: " + objClassDefNew.get_Name());
		        return objClassDefNew;
		    }
		}
