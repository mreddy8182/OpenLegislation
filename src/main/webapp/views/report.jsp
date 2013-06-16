<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ArrayList,gov.nysenate.openleg.model.Report,java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <link href="<%=request.getContextPath()%>/bootstrap.css" rel="stylesheet">
 <link href="<%=request.getContextPath()%>/bootstrap-responsive.css" rel="stylesheet">
<% ArrayList<Report> report= (ArrayList<Report>)request.getAttribute("reportList"); %>
		

<title>Reports</title>

</head>
<jsp:include page="/reportHeader.jsp"/>

<body>

<form id="form1" name="report" method="get">

<table class="table table-hover" id="errortable" >
 


 <% for(Report r:report){ %>
       
     <tr class="span12" style="background-color:white">
     <td class="span12">
     <%
         java.util.Date date=r.getDate();
     %>
    <a href="<%=request.getContextPath() %>/ErrorLog?id=<%=r.getId() %>&date=<%=date %>&val=report">  ERROR REPORT : <%= date%></a>
     </td>
    
     
     
     </tr>
 
   <%   }%>   
 </table>
 </form>
 <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>

 

</body>
</html>