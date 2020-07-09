// Initialize Firebase
var config = {
    apiKey: "AIzaSyB01kWkQt23PZusduWnG1VEl09E7OvYNoo",
    authDomain: "sisbar.firebaseapp.com",
    databaseURL: "https://sisbar.firebaseio.com",
    projectId: "sisbar",
    storageBucket: "sisbar.appspot.com",
    messagingSenderId: "323025218092",
    appId: "1:323025218092:web:cbd29abb9c75b72fa6ff89",
    measurementId: "G-ZXEDPP8LWD"
};

firebase.initializeApp(config);


const dbRef = firebase.database().ref();

const usersRef = dbRef.child('admins');
const userListUI = document.getElementById("userList");

usersRef.on("child_added", snap => {

	let user = snap.val();

	let $li = document.createElement("li");
	$li.innerHTML = snap.key;
	$li.setAttribute("child-key", snap.key);
	var content = document.createTextNode(snap.key);
	$li.appendChild(content);
	$li.addEventListener("click", userClicked)
	userListUI.append($li);

});


function userClicked(e) {

	var userID = e.target.getAttribute("child-key");

	const userRef = dbRef.child('admins/' + userID);
	const userDetailUI = document.getElementById("userDetail");

	userDetailUI.innerHTML = ""

	userRef.on("child_added", snap => {


		var $p = document.createElement("p");
		$p.innerHTML = snap.key  + " - " +  snap.val()
		userDetailUI.append($p);


	});

}


