	<div id="edit" th:fragment="edit">
		<form id="submitForm" action="" th:action="@{/[(${objectName})]}" method="POST" th:object="${item}">
		<table name="submitForm"  class="table table-bordered" align="center">
			<tbody>
				<!-- Netex Id -->
				<div th:replace="/html/simpleInput :: input('[(${objectName})].id','item.[(${objectName})].id','Id','true')"></div>
				<!-- Name -->
				<div th:replace="/html/inputMulti :: inputMulti('[(${objectName})].name.value','item.[(${objectName})].name.value','[(${objectName})].name.lang','Name','true')"></div>
				<!-- version -->
				<div th:replace="/html/simpleInput :: input('[(${objectName})].version','item.[(${objectName})].version','Version','true')"></div>
								
				<!-- Operator -->
				<div th:replace="/html/selectOption :: option('operator','Operator name','item.operator','/BaseInfo')"/>
								
				<tr>
					<td><input type="submit" value="Submit" /></td>
				</tr>
			</tbody>
		</table>
		</form>
		
		
	</div>