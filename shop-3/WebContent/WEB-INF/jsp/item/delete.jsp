<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>상품 삭제</title>
</head>
<body>
	<h2>상품 삭제</h2>
	<table>
		<tr>
			<td>상품명</td>
			<td>${item.name }</td>
		</tr>
		<tr>
			<td>상품가격</td>
			<td>${item.price }</td>
		</tr>
		<tr>
			<td>상품 이미지</td>
			<td><img src="../img/${item.pictureUrl }" width = "200" height="250"></td>
		</tr>
		<tr>
			<td>상품설명</td>
			<td>${item.description }</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="button" value="삭제" onclick="location.href='delete.shop?id=${item.id}'">
				<input type="button" value="목록" onclick="location.href='list.shop'">
			</td>
		</tr>
	</table>
</body>
</html>