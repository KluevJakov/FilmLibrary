let userSearch = document.getElementById("userSearch");
let filmSearch = document.getElementById("filmSearch");

getUsers();
getFilms();

userSearch.addEventListener("keyup", searchUsers);
filmSearch.addEventListener("keyup", searchFilms);

function searchUsers(e) {
    getUsers(e.target.value);
}

function searchFilms(e) {
    getFilms(e.target.value);
}

function getUsers(search) {
    var xhr = new XMLHttpRequest();
    let req = '/users';
    if (search && search.length != 0) {
        req += "?search="+search;
    }
    xhr.open('GET', req, false);
    xhr.send();
    if (xhr.status != 200) {
      console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
      let users = JSON.parse(xhr.responseText);
      console.log(users);
      drawUsers(users);
    }
}

function getFilms(search) {
    var xhr = new XMLHttpRequest();
    let req = '/films';
    if (search && search.length != 0) {
        req += "?search="+search;
    }
    xhr.open('GET', req, false);
    xhr.send();
    if (xhr.status != 200) {
      console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
      let films = JSON.parse(xhr.responseText);
      console.log(films);
      drawFilms(films);
    }
}

function drawUsers(users) {
    let usersBlock = document.getElementById("usersBlock");
    usersBlock.innerHTML = "";
    users.forEach(e => {
        let newItem = document.createElement("li");
        newItem.classList.add("list-group-item");
        newItem.classList.add("selectable");
        newItem.classList.add("mb-1");
        newItem.addEventListener('click', h => {
            editUserShowModal(e.id);
        });
        let newContent = document.createTextNode(e.login + " " + e.email);
        newItem.appendChild(newContent)
        usersBlock.appendChild(newItem);
    });
}

function drawFilms(films) {
    let filmsBlock = document.getElementById("filmsBlock");
    filmsBlock.innerHTML = "";
    films.forEach(e => {
        let newItem = document.createElement("li");
        newItem.classList.add("list-group-item");
        newItem.classList.add("selectable");
        newItem.classList.add("mb-1");
        newItem.addEventListener('click', h => {
            editFilmShowModal(e.id);
        });
        let newContent = document.createTextNode(e.title + " (" + e.year + ")");
        newItem.appendChild(newContent)
        filmsBlock.appendChild(newItem);
    });
}

function addFilm() {
    let filmModalForm = document.getElementById("filmModalForm");
    console.log(filmModalForm.checkValidity());
    if (filmModalForm.checkValidity() === false){
        filmModalForm.reportValidity();
        return;
    }

    let body = {
        id: null,
        title: document.getElementById("title").value,
        genre: document.getElementById("genre").value,
        ageRestriction: parseInt(document.getElementById("ageRestriction").value),
        year: parseInt(document.getElementById("year").value),
        country: document.getElementById("country").value,
        director: document.getElementById("director").value,
        scenarist: document.getElementById("scenarist").value,
        producer: document.getElementById("producer").value,
        budget: parseInt(document.getElementById("budget").value),
        fee: parseInt(document.getElementById("fee").value),
        time: parseInt(document.getElementById("time").value),
        image: document.getElementById("image").value
    };

    console.log(body);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/addFilm', false);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(body));
    if (xhr.status != 200) {
      console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
        filmSearch.value = body.title;
        getFilms(body.title);
        clearFields();
        let myModalEl = document.getElementById('filmModal');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}

function editFilmShowModal(filmId) {
    let modal = new bootstrap.Modal(document.getElementById('filmEditModal'), {  keyboard: false });
    modal.show();

    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/film?id='+filmId, false);
    xhr.send();
    if (xhr.status != 200) {
        console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
        let film = JSON.parse(xhr.responseText);
        console.log(film);
        document.getElementById("editFilmBtn").setAttribute("name", filmId);
        document.getElementById("removeFilmBtn").setAttribute("name", filmId);
        document.getElementById("etitle").value = film.title;
        document.getElementById("egenre").value = film.genre;
        document.getElementById("eageRestriction").value = film.ageRestriction;
        document.getElementById("eyear").value = film.year;
        document.getElementById("ecountry").value = film.country;
        document.getElementById("edirector").value = film.director;
        document.getElementById("escenarist").value = film.scenarist;
        document.getElementById("eproducer").value = film.producer;
        document.getElementById("ebudget").value = film.budget;
        document.getElementById("efee").value = film.fee;
        document.getElementById("etime").value = film.time;
        document.getElementById("eimage").value = film.image;
    }
}

function editFilm() {
    let filmEditModalForm = document.getElementById("filmEditModalForm");
    console.log(filmEditModalForm.checkValidity());
    if (filmEditModalForm.checkValidity() === false){
        filmEditModalForm.reportValidity();
        return;
    }

    let body = {
        id: parseInt(document.getElementById("editFilmBtn").getAttribute("name")),
        title: document.getElementById("etitle").value,
        genre: document.getElementById("egenre").value,
        ageRestriction: parseInt(document.getElementById("eageRestriction").value),
        year: parseInt(document.getElementById("eyear").value),
        country: document.getElementById("ecountry").value,
        director: document.getElementById("edirector").value,
        scenarist: document.getElementById("escenarist").value,
        producer: document.getElementById("eproducer").value,
        budget: parseInt(document.getElementById("ebudget").value),
        fee: parseInt(document.getElementById("efee").value),
        time: parseInt(document.getElementById("etime").value),
        image: document.getElementById("eimage").value
    };

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/addFilm', false);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(body));
    if (xhr.status != 200) {
      console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
        filmSearch.value = body.title;
        getFilms(body.title);
        clearFields();
        let myModalEl = document.getElementById('filmEditModal');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}

function removeFilm() {
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE', '/removeFilm?id='+parseInt(document.getElementById("removeFilmBtn").getAttribute("name")), false);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send();
    if (xhr.status != 200) {
      console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
        getFilms(filmSearch.value);
        let myModalEl = document.getElementById('filmEditModal');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}

function editUserShowModal(userId) {
    let modal = new bootstrap.Modal(document.getElementById('userEditModal'), {  keyboard: false });
    modal.show();

    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/userById?id='+userId, false);
    xhr.send();
    if (xhr.status != 200) {
        console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
        let user = JSON.parse(xhr.responseText);
        console.log(user);
        document.getElementById("editUserBtn").setAttribute("name", userId);
        document.getElementById("removeUserBtn").setAttribute("name", userId);
        document.getElementById("login").value = user.login;
        document.getElementById("email").value = user.email;
    }
}

function editUser() {
    let userEditModalForm = document.getElementById("userEditModalForm");
    console.log(userEditModalForm.checkValidity());
    if (userEditModalForm.checkValidity() === false){
        userEditModalForm.reportValidity();
        return;
    }

    let body = {
        id: parseInt(document.getElementById("editUserBtn").getAttribute("name")),
        login: document.getElementById("login").value,
        email: document.getElementById("email").value
    };

    var xhr = new XMLHttpRequest();
    xhr.open('PUT', '/editUser', false);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(body));
    if (xhr.status != 200) {
      console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
        userSearch.value = body.login;
        getUsers(body.login);
        let myModalEl = document.getElementById('userEditModal');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}

function removeUser() {
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE', '/removeUser?id='+parseInt(document.getElementById("removeUserBtn").getAttribute("name")), false);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send();
    if (xhr.status != 200) {
      console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
        getUsers(userSearch.value);
        let myModalEl = document.getElementById('userEditModal');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}

function clearFields() {
    document.getElementById("title").value = null;
    document.getElementById("genre").value = null;
    document.getElementById("ageRestriction").value = null;
    document.getElementById("year").value = null;
    document.getElementById("country").value = null;
    document.getElementById("director").value = null;
    document.getElementById("scenarist").value = null;
    document.getElementById("producer").value = null;
    document.getElementById("budget").value = null;
    document.getElementById("fee").value = null;
    document.getElementById("time").value = null;
    document.getElementById("image").value = null;
}