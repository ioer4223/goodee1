<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@include file = "/WEB-INF/jsp/jspHeader.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>���ȭ��</title>
</head>
<body>
	<h2>����� ����</h2>
	<table>
		<tr>
			<td>������ ID</td>
			<td>${loginUser.userId}</td>
		</tr>	
		<tr>
			<td>������ �̸�</td>
			<td>${loginUser.userName}</td>
		</tr>	
		<tr>
			<td>�����ȣ</td>
			<td>${loginUser.postCode}</td>
		</tr>	
		<tr>
			<td>�ּ�</td>
			<td>${loginUser.address}</td>
		</tr>	
		<tr>
			<td>��ȭ��ȣ</td>
			<td>${loginUser.phoneNo}</td>
		</tr>	
		<tr>
			<td>�̸���</td>
			<td>${loginUser.email}</td>
		</tr>	
	</table><br><br>
	
	<h2>���Ż�ǰ ���</h2>
	<table>
		<tr>
			<th>
				��ǰ��
			</th>
			<th>
				��ǰ����
			</th>
			<th>
				�ֹ�����
			</th>
			<th>
				���ݾ�
			</th>
		</tr>
		<c:forEach items="${itemList}" var="itemSet">
			<tr>
				<td>${itemSet.item.name }</td>
				<td>${itemSet.item.price }��</td>
				<td>${itemSet.quantity }��</td>
				<td>${itemSet.quantity*itemSet.item.price }��</td>
			</tr>			
		</c:forEach>
	</table><br>
	
	<b>�ѱݾ�: ${totalAmount }</b>
	
	<br><br><a href="end.shop">Ȯ��</a>&nbsp;
	<a href="../item/list.shop">��Ϻ���</a>
</body>
</html>