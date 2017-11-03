package cn.ffcs.uom.organization.action;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.manager.OrganizationRelationNameVoManager;
import cn.ffcs.uom.organization.model.OrganizationRelation;

@Controller
@Scope("prototype")
public class OrganizationPathComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7940796478315341585L;
	@Getter
	@Setter
	private Rows pathRows;
	@Getter
	@Setter
	private Window organizationPathWindow;

	private OrganizationRelationNameVoManager organizationRelationNameVoManager = (OrganizationRelationNameVoManager) ApplicationContextUtil
			.getBean("organizationRelationNameVoManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, this);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void onCreate$organizationPathWindow() throws Exception {

		this.organizationPathWindow.setTitle("组织路径查看");

		List<OrganizationRelation> organizationRelationList = (List<OrganizationRelation>) arg
				.get("organizationRelationList");

		if (organizationRelationList == null
				|| organizationRelationList.size() == 0) {
			ZkUtil.showError("组织ID错误", "提示信息");
			this.organizationPathWindow.onClose();
			return;
		}

		List<String> pathList = this.organizationRelationNameVoManager
				.getOrganizationTreePath(organizationRelationList);

		int pathCount = 1;

		if (pathList != null) {
			for (String path : pathList) {
				Row row = new Row();
				row.setParent(this.pathRows);
				Label titleLabel = new Label();
				titleLabel.setValue("路径" + pathCount++);
				Label pathLabel = new Label();
				pathLabel.setValue(path);
				row.appendChild(titleLabel);
				row.appendChild(pathLabel);
			}
		}
	}
}
