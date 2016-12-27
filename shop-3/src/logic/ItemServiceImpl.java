package logic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;


/*
 * @Service �� �⺻������ @Component�� (����)
 * Component��� (�ش� ���, Ȥ�� Ŭ������)��üȭ + Controller�� Model�� �߰� ����.
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired //ItemDao��ü�� �����ϰ� �ȴ�. -> ������ �������̽� �̱⶧���� ������ �� ����Ŭ������ ItemDaoImpl��ü�� ���Ե�
	private ItemDao dao;
	
	@Override
	public List<Item> getItemList() {
		return dao.findAll();
	}

	@Override
	public Item getItemById(Integer id) {
		return dao.selectById(id);
	}

	@Override
	public void entryItem(Item item, HttpServletRequest request) {
		//���� ���ε�
		if(item.getPicture() != null && !item.getPicture().isEmpty()){
			uploadFileCreate(item.getPicture(),request);
		}
		
		//db�� ���
		dao.create(item);
	}

	//picture: ���ε�� ������ ������ �����ϰ� �ִ� ��ü.
	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request) {
		String uploadPath= request.getServletContext().getRealPath("/")+"/img/";
		FileOutputStream fos= null;
		
		try{
			fos = new FileOutputStream(uploadPath + picture.getOriginalFilename());
			InputStream in = picture.getInputStream();
			int data;
			
			while((data=in.read())!=-1){
				fos.write(data);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(fos != null){
					fos.flush();
					fos.close();
				}
			}catch(IOException e){}
		}
	}

	@Override
	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()){
			uploadFileCreate(item.getPicture(),request);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		
		dao.update(item);
	}

	@Override
	public void itemDelete(Integer id) {
		dao.delete(id);
	}

}
