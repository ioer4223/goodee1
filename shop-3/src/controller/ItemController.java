package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ItemService;

/*
 * @Controller�� �⺻������ @Component�� �ش��Ѵ�.(����)
 * Component��� (�ش� ���, Ȥ�� Ŭ������)��üȭ + Controller���
 * 
 * �� ItemController��ü�� ��üȭ �ɶ�,�ʿ��� ��ü�� � ����
 * @Autowired : ItemService��ü�� �����϶�� ��. (�����δ� ItemServiceImpl��ü�� ����)
 * 
 * 
 */


@Controller
public class ItemController {
	
	@Autowired //ItemService��ü�� �����ϰ� �ȴ�. -> ������ �������̽� �̱⶧���� ������ �� ����Ŭ������ ItemServiceImpl��ü�� ���Ե�
	private ItemService itemService; 
	
	@RequestMapping("item/list") // item/list.shop�� ȣ�� �Ǵ°�� shop-3-servlet.xml�� �� �������� @Requestmapping��   handlerMapping�� ���Ͽ�(xml �ּ� ����), /WEB-INF/jsp/item/list.jsp�� �����Ѵ�.
	public ModelAndView list(){
		
		//itemList : item���̺��� ������ ���� Item��ü�� �����Ͽ� ���Ϲ޴´�.
		List<Item> itemList = itemService.getItemList();
		
		//ModelAndView : ������ + ���� �̸��� ���� (������Ʈ ��ü�� ���� �ʰ�, �����͸� ������� ���� �����ϴ�. �Ʒ��� ��� addObject�� ���� itemList�� ����ִ� Item��ü�� �Ѱ��ְ� �ȴ�.)
						//�̶�, �伳���� �ϴ� �κ��� ���µ�, �� ���� xml������ ���ؼ� , '�̸�'�� �⺻���� �䰡 ������ �ȴ�. (���⼱ item/list ��ο� shop-3-servlet.xml�� �� ������ ��ο� ����, WEB-INF/jsp/item/list.jsp�� �����)
		ModelAndView mav = new ModelAndView();
		mav.addObject("itemList",itemList); //��ܿ� ��Ʈ����Ʈ�� ��� el�±׷� ���������
		return mav;
	}
	
	@RequestMapping("item/detail")
	public ModelAndView detail(Integer id){ //detail.jsp ����� �ҷ��ö�, �Ķ���ͷ� id���� �Ѿ�´�
		Item item = itemService.getItemById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("item",item); //view�ܿ� ��Ʈ����Ʈ�� ���
		return mav;
	}
	
	@RequestMapping("item/create")
	public ModelAndView create(){
		ModelAndView mav = new ModelAndView("item/add");
		mav.addObject(new Item());
		return mav;
	}
	@RequestMapping("item/register")
	public ModelAndView register(@Valid Item item, BindingResult bindingResult, HttpServletRequest request){
		
		//@Valid : Item ��ü�� �����Ҷ�, ��ȿ�� ������ ���� �϶�� ���̴�. Item Ŭ������ �ִ� ������̼ǰ� ����ȴ�. (������ Validator �������̽�, ����Ŭ������ �ʿ������)
		//@Valid�� �̿��� �����Ҷ�, �ݵ�� ���� �Ķ���ͷδ� BindingResult���� �Ѵ�.
		ModelAndView mav = new ModelAndView("item/add");
		if(bindingResult.hasErrors()){
			mav.getModel().putAll(bindingResult.getModel());
			return mav;
		}
		itemService.entryItem(item,request);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
	}
	
	@RequestMapping("item/edit")
	public ModelAndView edit(Integer id){
		
		return detail(id);
	}
	@RequestMapping("item/update")
	public ModelAndView update(@Valid Item item, BindingResult bindingResult, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("item/edit");
		
		if(bindingResult.hasErrors()){
			mav.getModel().putAll(bindingResult.getModel());
			return mav; //������ ������ �߻��ϸ� edit.jsp ��ܿ� �ӹ����� item/edit�� �����ؾ���(update.jsp ��� �����Ƿ� ���ڸ��� �ӹ�����)
		}
		itemService.itemUpdate(item,request);
		mav.setViewName("redirect:/item/list.shop"); //redirect�� , �����͸� �Ѱ����� �ʾƵ� �ȴ�. (������ ������ �ȳѾ)
		return mav;
	}
	@RequestMapping("item/confirm")
	public ModelAndView confirm(Integer id){
		Item item = itemService.getItemById(id);
		ModelAndView mav = new ModelAndView("item/delete");
		
		mav.addObject("item",item);
		return mav;
	}
	
	
	
	/*
	 * delete �� ���, item��ü�� ������, �����͸� �ٽ� �������� �ʿ䰡 ����.(�����ߴµ� �Ѱ��� ����������)
	 * �� ��� �Ʒ��Ͱ��� item��ü ���� ������ �ٷ� redirect �����ֵ��� �ڵ带 �ۼ��ص� ������ ����. (redirect�� �ϰԵǸ� �����͸� �ѱ��� �ʴ´�.)
	 */
	@RequestMapping("item/delete")
//	public ModelAndView delete(Integer id){
//		ModelAndView mav = new ModelAndView();
//		itemService.itemDelete(id);
//		mav.setViewName("redirect:/item/list.shop");
//		return mav;
//	}

	public String delete(Integer id){
		itemService.itemDelete(id);
		return "redirect:/item/list.shop";
	}
	
	

}
