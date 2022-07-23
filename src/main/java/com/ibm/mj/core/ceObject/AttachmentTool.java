package com.ibm.mj.core.ceObject;

import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;

import filenet.vw.api.VWAttachment;
import filenet.vw.api.VWAttachmentType;
import filenet.vw.api.VWException;
import filenet.vw.api.VWLibraryType;

public class AttachmentTool {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static VWAttachment getDocAttachment (ObjectStore os, Document doc) {
		VWAttachment att = new VWAttachment();
		try {		
			att.setLibraryName(os.get_SymbolicName());
			att.setLibraryType(VWLibraryType.LIBRARY_TYPE_CONTENT_ENGINE);
			att.setType(VWAttachmentType.ATTACHMENT_TYPE_DOCUMENT);
			att.setAttachmentName("Attachment_name");
			att.setAttachmentDescription("Added by code");
			att.setId(doc.get_VersionSeries().get_Id().toString());
		} catch (VWException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return att;
	}

	public static VWAttachment getFolderAttachment (ObjectStore os, Folder folder) {
		VWAttachment att = new VWAttachment();
		try {		
			att.setLibraryName(os.get_SymbolicName());
			att.setLibraryType(VWLibraryType.LIBRARY_TYPE_CONTENT_ENGINE);
			att.setType(VWAttachmentType.ATTACHMENT_TYPE_FOLDER);
			att.setAttachmentName("Attachment_name");
			att.setAttachmentDescription("Added by code");
			att.setId(folder.get_Id().toString());
		} catch (VWException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return att;
	}

	public static VWAttachment getCustomObjectAttachment (ObjectStore os, CustomObject custo) {
		VWAttachment att = new VWAttachment();
		try {		
			att.setLibraryName(os.get_SymbolicName());
			att.setLibraryType(VWLibraryType.LIBRARY_TYPE_CONTENT_ENGINE);
			att.setType(VWAttachmentType.ATTACHMENT_TYPE_CUSTOM_OBJECT);
			att.setAttachmentName("Attachment_name");
			att.setAttachmentDescription("Added by code");
			att.setId(custo.get_Id().toString());
		} catch (VWException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return att;
	}
}
