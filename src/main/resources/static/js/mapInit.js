// src/main/resources/static/js/mapInit.js
let mapInit;
let markers = [];
let directionsService;
let directionsRenderer;

async function fetchCampuses(query = '') {
    const response = await fetch(`/api/campuses${query}`);
    return await response.json();
}

function addMarkers(campuses) {
    markers.forEach(marker => marker.setMap(null)); // Clear existing markers
    markers = [];
    campuses.forEach((campus, index) => {
        const marker = new google.maps.Marker({
            position: { lat: campus.latitude, lng: campus.longitude },
            map: mapInit,
            title: campus.nomC,
            label: {
                text: campus.nomC,
                color: 'black',
                fontSize: '14px',
                fontWeight: 'bold',
                position: 'bottom'
            }
        });
        markers.push(marker);
    });
}

function renderCampusList(campuses) {
    const listView = document.getElementById('listView');
    listView.innerHTML = '';
    campuses.forEach((campus, index) => {
        const campusCard = document.createElement('div');
        campusCard.className = 'bg-white rounded-lg shadow-sm p-4 hover:shadow-md transition-shadow duration-300';
        campusCard.innerHTML = `
            <div class="flex items-center justify-between mb-2">
                <h3 class="text-lg font-semibold">${campus.nomC}</h3>
                <span class="bg-gray-200 text-gray-700 px-2 py-1 rounded-full text-sm">${campus.ville}</span>
            </div>
            <p class="text-gray-600 mb-2">${campus.adresse}</p>
            <button class="viewMapBtn text-blue-500 hover:underline" data-index="${index}">View on Map</button>
        `;

        listView.appendChild(campusCard);
    });

    document.querySelectorAll('.viewMapBtn').forEach(button => {
        button.addEventListener('click', (event) => {
            const index = event.target.getAttribute('data-index');
            if (markers[index]) {
                listView.classList.add('hidden');
                mapView.classList.remove('hidden');
                mapInit.setCenter(markers[index].getPosition());
                mapInit.setZoom(15);

                // Clear the directions
                directionsRenderer.setDirections({ routes: [] });
            } else {
                console.error('Marker not found for index:', index);
            }
        });
    });
}

function initMap() {
    mapInit = new google.maps.Map(document.getElementById('mapView'), {
        center: { lat: 43.6119, lng: 3.8772 },
        zoom: 10
    });

    directionsService = new google.maps.DirectionsService();
    directionsRenderer = new google.maps.DirectionsRenderer();
    directionsRenderer.setMap(mapInit);

    fetchCampuses().then(campuses => {
        renderCampusList(campuses);
        addMarkers(campuses);
    });
}

function loadGoogleMapsApi() {
    const script = document.createElement('script');
    script.src = `https://maps.googleapis.com/maps/api/js?key=${googleApiKey}&callback=initMap`;
    script.async = true;
    script.defer = true;
    document.head.appendChild(script);
}

document.addEventListener('DOMContentLoaded', () => {
    loadGoogleMapsApi();
});