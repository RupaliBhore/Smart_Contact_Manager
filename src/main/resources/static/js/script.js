console.log("this is script file");

const toggleSidebar=()=>
{
	//chek karo sidebar dikha raha he kya dikha raha he to band  true he to bandh
	if($('.sidebar').is(":visible"))
	{
		$(".sidebar").css("display","none");
		
		$(".content").css("margin-left","0%")
		
	}else{
		//nahi dikha raha he to show karo
		
		$(".sidebar").css("display","block");
		
		$(".content").css("margin-left","20%")
		
	}
	
};

const search=()=>{
	console.log("searching......");

	let query=$("#search-input").val();
	
//query blank hogi matlab query kuch di nahi hogi to search box hide ho jayega agar query diyi he to search result show ho jayega
	if(query==" ")
	{
		$(".search-result").hide();

	}else{

		console.log(query);

		//sending request to servlet
		
		
		//let url= 'http://localhost:8080/search?query';
         
		let url= 'http://localhost:8080/search/${query}';
		//let url='http://localhost:8080/search';

		//jquery ka prommis he
		//jab maine fetch(url)call kiya then me jo responase aayega o (response) me aayega..fir respose ko json me pass karke json me convert karke fir
		// se return kiya return response.json();ise
		//aur yaha se jo data return hoga dusare then me data me mil jayega .then(data) koi bhi vaeible le sakte ho maine data varible liya he
		fetch(url)
		.then((response) => {
	     return response.json();
	     
		})
		.then((data)=>{

			//data ko access karane padega json ko access karne ke liye
               console.log(data);

		});

		$(".search-result").show();

	}

	

};