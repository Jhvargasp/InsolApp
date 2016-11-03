
	
	
var models = {};
	
models.object = getViewObjectDefaults;
models.form = getFormDefaults;
	
	
	
function getViewObjectDefaults(name){
		
	var obj = {};
		
	obj.loading = false;
	obj.error = false;
	obj.list = [];
	if(name !== undefined){
		obj.get = 'get' + fCap(name) + 's';
		obj.save = 'save' + fCap(name) + 's';	
		obj.remove = 'remove' + fCap(name) + 's';	
	}
	return obj;
}
		
function getFormDefaults(name){
	
	var form = {};
	form.loading = '';
	form.submitted = false;
	form.error = false;
	form.succes = false;
	return form;
}

function fCap(str){
	return str.charAt(0).toUpperCase() + str.slice(1);
}