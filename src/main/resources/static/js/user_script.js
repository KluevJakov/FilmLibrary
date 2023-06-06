let filmSearch = document.getElementById("filmSearch");

getFilms();
getPersonalRecs();

filmSearch.addEventListener("keyup", searchFilms);

function getPersonalRecs() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/recs', false);
    xhr.send();
    if (xhr.status != 200) {
      console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
      let recs = JSON.parse(xhr.responseText);
      drawRecs(recs);
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

function searchFilms(e) {
    getFilms(e.target.value);
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
            viewFilmShowModal(e.id);
        });
        let newContent = document.createTextNode(e.title + " (" + e.year + ")");
        newItem.appendChild(newContent)
        filmsBlock.appendChild(newItem);
    });
}

function drawRecs(recs) {
    let recsBlock = document.getElementById("recsBlock");
    recsBlock.innerHTML = "";
    recs.forEach(e => {
        let newItem = document.createElement("div");
        newItem.classList.add("card");
        newItem.classList.add("selectable");
        newItem.style.width = "15rem";
        newItem.classList.add("m-2");
        newItem.addEventListener('click', h => {
            viewFilmShowModal(e.id);
        });

        let imgContent = document.createElement("img");
        imgContent.classList.add("card-img-top");
        imgContent.src = e.image;
        imgContent.style.height = "200px";
        imgContent.style.objectFit = "cover";

        let divContent = document.createElement("div");
        divContent.classList.add("card-body");

        let pContent = document.createElement("p");
        pContent.classList.add("card-text");

        let newContent = document.createTextNode(e.title + " (" + e.year + ") - " + e.country);

        pContent.appendChild(newContent);
        divContent.appendChild(pContent);
        newItem.appendChild(imgContent);
        newItem.appendChild(divContent);
        recsBlock.appendChild(newItem);
    });
}

function viewFilmShowModal(filmId) {
    let modal = new bootstrap.Modal(document.getElementById('filmView'), {  keyboard: false });
    modal.show();

    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/filmExt?id='+filmId, false);
    xhr.send();
    if (xhr.status != 200) {
        console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
        let film = JSON.parse(xhr.responseText);
        console.log(film);
        document.getElementById("btn1").setAttribute("name", filmId);
        document.getElementById("btn2").setAttribute("name", filmId);
        document.getElementById("btn3").setAttribute("name", filmId);
        document.getElementById("btn4").setAttribute("name", filmId);

        if (film.like != 0) {
            if (film.like == 1) {
                document.getElementById("btn1").disabled = true;
                document.getElementById("btn2").disabled = false;
            } else {
                document.getElementById("btn1").disabled = false;
                document.getElementById("btn2").disabled = true;
            }
        }

        if (film.view != 0) {
            if (film.view == 1) {
                document.getElementById("btn3").disabled = true;
                document.getElementById("btn4").disabled = false;
            } else {
                document.getElementById("btn3").disabled = false;
                document.getElementById("btn4").disabled = true;
            }
        }

        document.getElementById("title").value = film.title;
        document.getElementById("genre").value = film.genre;
        document.getElementById("ageRestriction").value = film.ageRestriction;
        document.getElementById("year").value = film.year;
        document.getElementById("country").value = film.country;
        document.getElementById("director").value = film.director;
        document.getElementById("scenarist").value = film.scenarist;
        document.getElementById("producer").value = film.producer;
        document.getElementById("budget").value = film.budget;
        document.getElementById("fee").value = film.fee;
        document.getElementById("time").value = film.time;
        document.getElementById("image").src = film.image;
    }
}

function btn1(btn) {
    var xhr = new XMLHttpRequest();
    let req = "";
    if (btn == 1) {
        req = '/action?id='+document.getElementById("btn1").getAttribute("name")+"&action=1";
    } else if (btn == 2) {
        req = '/action?id='+document.getElementById("btn2").getAttribute("name")+"&action=2";
    } else if (btn == 3) {
        req = '/action?id='+document.getElementById("btn3").getAttribute("name")+"&action=3";
    } else if (btn == 4) {
        req = '/action?id='+document.getElementById("btn4").getAttribute("name")+"&action=4";
    }
    xhr.open('GET', req, false);
    xhr.send();
    if (xhr.status != 200) {
      console.log(xhr.status + ' : ' + xhr.statusText);
    } else {
        let myModalEl = document.getElementById('filmView');
        let modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    }
}