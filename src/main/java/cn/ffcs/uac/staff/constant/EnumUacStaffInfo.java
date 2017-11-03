package cn.ffcs.uac.staff.constant;

public enum EnumUacStaffInfo {
	UAC_STAFF_EDIT_ZUL("/pages/uac_staff/uac_staff_edit.zul"),
	CERT_NOT_EXIST("证件信息不存在"),
	CONTACT_NOT_EXIST("联系人信息不存在"),
	ATTACHED_NOT_EXIST("人员附加信息不存在"),
	OPERATE_SUCCESS("操作成功"),
	UAC_STAFF_LISTBOX_EXT_ZUL("/pages/uac_staff/comp/uac_staff_listbox_ext.zul"),
	ADD("add"),
	MOD("mod"),
	VIEW("view"),
	ON_OK("onOK"),
	CERT_TYPE_IDCARD("1"),
	REAL_NAME_YES("1"),
	REAL_NAME_NO("0"),
	WORKPROP_W_AGENT("40000"),
	WORKPROP_W_OTHER("90000"),
	WORKPROP_W_PROVIDER("50000"),
	TRUE(""),
	FALSE("");
	
	private String value;
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	private EnumUacStaffInfo(String value) {
		this.value = value;
	}
	
	@Override
    public String toString() {
        return this.value;
    }
}
