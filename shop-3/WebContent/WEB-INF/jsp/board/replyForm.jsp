<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@ include file="/WEB-INF/jsp/jspHeader.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>��۴ޱ�</title>
</head>
<body>
<form action = "reply.shop" method = "post" name="boardform">
	<input type ="hidden" name="num" value="${board.num }"> <%--hidden�Ӽ��� input�� ���ۿ����� �������� ������� �ۼ� --%>
	<input type ="hidden" name="ref" value="${board.ref }">
	<input type ="hidden" name="refLevel" value="${board.refLevel }">
	<input type ="hidden" name="refStep" value="${board.refStep }">
	
	<table border="1" cellspacing="0" cellpadding="0">
		<tr align="center">
			<td colspan="2">
				�Խ���
			</td>
		</tr>
		<tr>
			<td>�ۼ���</td>
			<td><input type="text" name="name"></td>
		</tr>
		<tr>
			<td>��й�ȣ</td>
			<td><input type="password" name="pass"></td>
		</tr>
		<tr>
			<td>����</td>
			<td><input type="text" name="subject"></td>
		</tr>
		<tr>
			<td>����</td>
			<td>
				<textarea name="content" rows="15" cols="67">
				</textarea>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<a href = "javascript:document.boardform.submit()">[��� ���]</a>
				<a href = "list.shop">[���]</a>
			</td>
		</tr>
	</table>
</form>
</body>
</html>