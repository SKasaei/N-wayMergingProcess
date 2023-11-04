pre{
	"Compare3UML is started  . . ".println();
}

rule Pack
	match b : BaseModel!Package with l : In!Package {
		compare : true	
}

rule Class
	match b : BaseModel!Class with l : In!Class{
		compare : b.name = l.name
}

rule Property
	match b : BaseModel!Property with l : In!Property{
		compare : b.name = l.name
}

rule Association2Association 
	match l : BaseModel!Association with r : In!Association { 
		compare : l.name = r.name 
} 

post{
	"Compare3UML is ended  . . ".println();
}