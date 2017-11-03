/**
 * 
 */
package cn.ffcs.uom.information.vo;

import java.util.List;

/**
 * @author 曾臻
 * @date 2012-11-20
 *
 */
public class ArticleVo {
	private long articleId;
	private String title;
	private String content;
	private String categoryCode;
	private String releaser;
	private String state;
	private long effectiveDate;
	private long expiredDate;
	private long creationDate;
	private long modifDate;
	private String displayType;
	
	private List<TargetOrgVo> targetOrgList;
	//保存附件“动作清单”，实际上附件已经生成，这里做的是和article的关联
	private List<AttachmentVo> attachmentList;
	//保存图片“动作清单”
	private List<ImageVo> imageList;
	//缩略图标识，null表示更新时忽略，0表示无缩略图
	private ThumbnailVo thumbnail;
	
	//非数据库字段
	private String releaserText;
	private String categoryText;
	private String stateText;
	private String displayTypeText;
	//作为确定是否在有效日期内的查询参数
	private long currentDate;
	//用于查询在指定范围内的文章
	private long beginDate;
	private long endDate;
	//用于匹配目标组织
	private Long currentOrgId;
	private String currentOrgUuid;
	//用于匹配多目标组织（如个人门户时，个人可带过多个组织）。与currentOrgId互斥
	private List<Long> userOrgIdList;
	private List<String> userOrgUuidList;
	//当前虚拟门户类型（"public","private","user"）
	private String currentType;
	//图片新闻中的简介
	private String briefText;
	
	
	public String getCurrentOrgUuid() {
		return currentOrgUuid;
	}
	public void setCurrentOrgUuid(String currentOrgUuid) {
		this.currentOrgUuid = currentOrgUuid;
	}
	public List<String> getUserOrgUuidList() {
		return userOrgUuidList;
	}
	public void setUserOrgUuidList(List<String> userOrgUuidList) {
		this.userOrgUuidList = userOrgUuidList;
	}
	public List<Long> getUserOrgIdList() {
		return userOrgIdList;
	}
	public void setUserOrgIdList(List<Long> userOrgIdList) {
		this.userOrgIdList = userOrgIdList;
	}
	public String getDisplayTypeText() {
		return displayTypeText;
	}
	public void setDisplayTypeText(String displayTypeText) {
		this.displayTypeText = displayTypeText;
	}
	public String getBriefText() {
		return briefText;
	}
	public void setBriefText(String briefText) {
		this.briefText = briefText;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public ThumbnailVo getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(ThumbnailVo thumbnail) {
		this.thumbnail = thumbnail;
	}
	public List<ImageVo> getImageList() {
		return imageList;
	}
	public void setImageList(List<ImageVo> imageList) {
		this.imageList = imageList;
	}
	public List<AttachmentVo> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<AttachmentVo> attachmentList) {
		this.attachmentList = attachmentList;
	}
	public String getCurrentType() {
		return currentType;
	}
	public void setCurrentType(String currentType) {
		this.currentType = currentType;
	}
	public Long getCurrentOrgId() {
		return currentOrgId;
	}
	public void setCurrentOrgId(Long currentOrgId) {
		this.currentOrgId = currentOrgId;
	}
	public List<TargetOrgVo> getTargetOrgList() {
		return targetOrgList;
	}
	public void setTargetOrgList(List<TargetOrgVo> targetOrgList) {
		this.targetOrgList = targetOrgList;
	}
	public String getCategoryText() {
		return categoryText;
	}
	public void setCategoryText(String categoryText) {
		this.categoryText = categoryText;
	}
	public long getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(long currentDate) {
		this.currentDate = currentDate;
	}
	public long getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(long beginDate) {
		this.beginDate = beginDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public String getReleaserText() {
		return releaserText;
	}
	public void setReleaserText(String releaserText) {
		this.releaserText = releaserText;
	}
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getReleaser() {
		return releaser;
	}
	public void setReleaser(String releaser) {
		this.releaser = releaser;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(long effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public long getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(long expiredDate) {
		this.expiredDate = expiredDate;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public long getModifDate() {
		return modifDate;
	}
	public void setModifDate(long modifDate) {
		this.modifDate = modifDate;
	}
}
