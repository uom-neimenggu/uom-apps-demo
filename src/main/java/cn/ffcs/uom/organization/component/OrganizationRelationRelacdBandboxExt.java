package cn.ffcs.uom.organization.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;

@Controller
@Scope("prototype")
public class OrganizationRelationRelacdBandboxExt extends Bandbox implements
		IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/comp/organizaiton_relacd_bandbox_ext.zul";
	/**
	 * positionMainWin
	 */
	@Getter
	@Setter
	private Listbox relaCd;

	@Getter
	private List<String> relaCdList;

	/**
	 * 构造函数
	 */
	public OrganizationRelationRelacdBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
	}

	/**
	 * 创建初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		List<NodeVo> relaCdList = UomClassProvider.getValuesList(
				"OrganizationRelation", "relaCd");
		ListboxUtils.rendererForEdit(this.relaCd, relaCdList);
	}

	/**
	 * 确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		Set<Listitem> set = this.relaCd.getSelectedItems();
		if (set != null) {
			Iterator<Listitem> it = set.iterator();
			relaCdList = new ArrayList<String>();
			String bandBoxValue = "";
			while (it.hasNext()) {
				Listitem li = it.next();
				String label = li.getLabel();
				String value = (String) li.getValue();
				if (!StrUtil.isEmpty(label) && !StrUtil.isEmpty(value)) {
					relaCdList.add(value);
					bandBoxValue += label + ",";
				}
			}
			if (StrUtil.isEmpty(bandBoxValue)) {
				bandBoxValue = bandBoxValue.substring(0,
						bandBoxValue.length() - 1);
			}
			this.setValue(bandBoxValue);
		}
		OrganizationRelationRelacdBandboxExt.this.close();
	}

	/**
	 * 取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		OrganizationRelationRelacdBandboxExt.this.close();
	}
}
