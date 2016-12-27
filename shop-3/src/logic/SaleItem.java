package logic;

import java.sql.Timestamp;

public class SaleItem {
	private Integer saleItemid; //�ֹ���ǰ ��ȣ
	private Integer itemId; //�ֹ���ǰ ��ȣ
	private Item item;	//�ֹ���ǰ����
	private Timestamp updateTime; //�ֹ��� �Ͻ�
	private Integer quantity; //�ֹ���ǰ�� ��������
	private Sale sale; //�ֹ�����
	public SaleItem(){
	}
	public SaleItem(Sale sale, int saleItemid, ItemSet itemSet, Timestamp currentTime){
		this.sale = sale;
		this.saleItemid = saleItemid;
		this.item = itemSet.getItem();
		this.updateTime = currentTime;
		this.quantity = itemSet.getQuantity();
	}
	public Integer getSaleItemid() {
		return saleItemid;
	}
	public void setSaleItemid(Integer saleItemid) {
		this.saleItemid = saleItemid;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Sale getSale() {
		return sale;
	}
	public void setSale(Sale sale) {
		this.sale = sale;
	}
	
}
