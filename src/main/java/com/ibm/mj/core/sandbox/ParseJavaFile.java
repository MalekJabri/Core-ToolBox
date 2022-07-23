package com.ibm.mj.core.sandbox;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.ibm.mj.core.configOperations.MethodInformation;
import com.ibm.mj.core.configOperations.Operation;

public class ParseJavaFile {
	public static void main(String[] args) throws Exception {
		List<Operation> operations = new ArrayList<Operation>();
		
	/*	operations.add(new Operation("ECM_Operations","D:/Workplace/ECMTools/",
				"D:/Workplace/ECMTools/src/main/java/com/ibm/mj/tools/ECMToolsOperations.java"));
		operations.add(new Operation("Merge_Operations","D:/Workplace/MergeOperations/",
				"D:/Workplace/MergeOperations/src/main/java/com/ibm/mj/merging/MergeOperations.java"));
		operations.add(new Operation("Sign_Operations","D:/Workplace/Sign_Operations/",
				"D:/Workplace/Sign_Operations/src/main/java/com/ibm/mj/signMerge/SignAndMergeOperations.java"));
		operations.add(new Operation("ICM_Calendar","D:/Work-Place/IMJ_Operations",
				"D:/Work-Place/IMJ_Operations/src/main/java/com/ibm/mj/calendar/CalOperations.java"));*/
		operations.add(new Operation("CRMS_Component","D:/Workplace/CRMS_Component/",
				"D:/Workplace/CRMS_Component/src/main/java/com/ibm/mj/tools/Sec_Operations.java"));
		for( Operation ope :operations){
		ParseJavaFile xmlfile = new ParseJavaFile(ope);	
		System.out.println(""+xmlfile.toString());
		}
	
	}	
	
	public ParseJavaFile(Operation ope) throws Exception {
		System.out.println(ope.getPathJavaClass());
		FileInputStream in = new FileInputStream(ope.getPathJavaClass());
		CompilationUnit cu = JavaParser.parse(in);	 
	if(cu==null){System.out.println("the document is empty");}
	
		Void arg = null;
		MethodVisitor methodvisitor = new MethodVisitor();
		methodvisitor.init();
		methodvisitor.visit(cu, arg);		
		XmlOperations.generateXML(ope.getPath(),ope.getOperationsName(),methodvisitor.getmethods());
	}
	
	private static class MethodVisitor extends VoidVisitorAdapter<Void> {
		public List<MethodInformation> methods ;
		public void init(){
			methods = new ArrayList<MethodInformation>();
		}
		public void visit(MethodDeclaration n, Void arg) {
			String Comment = "";
			// Retrieve only public Static Method
			if(n.getModifiers()==1){
				if(n.getComment()!=null) Comment = n.getComment().getContent().toString().trim();
				MethodInformation method = new MethodInformation(n.getName(), Comment);		        
				for(Parameter param : n.getParameters()){
					String parName = param.getId().toString();				
					boolean isArray = false;
					String type = param.getType().toStringWithoutComments();
					if(type.contains("[")) { 
						isArray=true;
						type = type.replace("[]", "").toLowerCase();
					}
					method.setAtttributes(parName, "", type, isArray, true);
					
				}
				
				if(!n.getType().toString().contains("void")){
					String returnValue = 	n.getType().toString();
			//	System.out.println("returnValue : "+  returnValue);
					boolean isArray =  returnValue.contains("[");
					returnValue = returnValue.replace("[]", "");
				//	System.out.println("returnValue : "+  returnValue);
					method.setAtttributes("Return", "", returnValue, isArray, false);
				}
				methods.add(method);
			}
			super.visit(n, arg);

		}
		public List<MethodInformation> getmethods (){
			return methods;
		}
	}
	
	
}
