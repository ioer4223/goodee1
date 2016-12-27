package logic;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;

@Service
public class ShopServiceImpl implements ShopService {
	
	@Autowired
	ItemDao itemDao;
	@Autowired
	UserDao userDao;
	@Autowired
	private SaleDao saleDao;
	@Autowired
	private SaleItemDao saleItemDao;
	@Override
	public Item getItemById(Integer id) {
		return itemDao.selectById(id);
	}

	@Override
	public Cart getCart() {
		return new Cart();
	}

	@Override
	public Integer calculateTotalAmount(List<ItemSet> itemList) {
		int total = 0;
		
		for(ItemSet is : itemList){
			total += is.getItem().getPrice() * is.getQuantity();
		}
		return total;
	}

	@Override
	public void createUser(User user) {
		userDao.create(user);
	}

	@Override
	public User getUserByIdPw(String userId, String password) {
		return userDao.selectIdPw(userId, password);
	}
	
	
	//sale���̺�� saleItem ���̺� ����
	//Sale �� Ŭ������ user���� + cart ������ ���Ͽ� ��ü�� �����Ѵ�.
	@Override
	public Sale checkEnd(User loginUser, Cart cart) {
		Sale sale = new Sale();
		sale.setSaleId(saleDao.getMaxSaleId()); //���� sale���̺��� �ִ� saleID������ +1 �� ���� ������ ��. (�ֹ���ȣ�� ��ġ���ʰ��Ͽ� sale���̺��� Ű�� ����ϱ�����)
		sale.setUser(loginUser); //�ֹ� ����� ����
		Timestamp currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
		sale.setUpdateTime(currentTime);
		List<ItemSet> itemList = cart.getItemList();
		for(int i =0; i<itemList.size();i++){
			ItemSet itemSet = (ItemSet)itemList.get(i);
			int saleItemId = i+1;
			SaleItem saleItem = new SaleItem(sale, saleItemId, itemSet, currentTime);
			sale.getSaleItemList().add(saleItem);
		}
		saleDao.createSale(sale);
		List<SaleItem> saleItemList= sale.getSaleItemList();
		for(SaleItem saleItem : saleItemList){
			saleItemDao.create(saleItem);
		}
		return sale;
	}

	@Override
	public User getUserById(String id) {
		return userDao.selectId(id);
	}

	@Override
	public List<Sale> saleList(String id) {
		return saleDao.list(id);
	}

	@Override
	public List<SaleItem> saleItemList(Integer saleId) {
		return saleItemDao.list(saleId);
	}

	@Override
	public List<User> userList() {
		return userDao.list();
	}

	@Override
	public List<User> userList(String[] idchks) {
		return userDao.list(idchks);
	}

}
