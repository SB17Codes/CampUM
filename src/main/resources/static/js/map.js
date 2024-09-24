let map;
let markers = [];
let directionsService;
let directionsRenderer;
let userLocationMarker;

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
            map: map,
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
                map.setCenter(markers[index].getPosition());
                map.setZoom(15);

                // Clear the directions
                directionsRenderer.setDirections({ routes: [] });
            } else {
                console.error('Marker not found for index:', index);
            }
        });
    });
}

function initMap() {
    map = new google.maps.Map(document.getElementById('mapView'), {
        center: { lat: 43.6119, lng: 3.8772 },
        zoom: 10
    });

    directionsService = new google.maps.DirectionsService();
    directionsRenderer = new google.maps.DirectionsRenderer();
    directionsRenderer.setMap(map);

    fetchCampuses().then(campuses => {
        renderCampusList(campuses);
        addMarkers(campuses);
    });
}

async function getItinerary(fromLat, fromLng, toLat, toLng, travelMode) {
    const response = await fetch(`/api/itinerary?origin=${fromLat},${fromLng}&destination=${toLat},${toLng}&mode=${travelMode}`);
    const data = await response.json();
    return data;
}

function showModal(distance, duration, travelMode, startAddress, endAddress) {
    const modal = document.getElementById('itineraryModal');
    const distanceText = document.getElementById('distanceText');
    const durationText = document.getElementById('durationText');
    const travelModeText = document.getElementById('travelModeText');
    const transportIcon = document.getElementById('transportIcon');
    const startAddressText = document.getElementById('startAddress');
    const endAddressText = document.getElementById('endAddress');

    distanceText.textContent = `Distance: ${distance}`;
    durationText.textContent = `Estimated Time: ${duration}`;
    travelModeText.textContent = travelMode.charAt(0).toUpperCase() + travelMode.slice(1);
    startAddressText.textContent = `Start Address: ${startAddress}`;
    endAddressText.textContent = `End Address: ${endAddress}`;

    // Set the appropriate icon for the travel mode
    const modeIcons = {
        driving: 'fa-car',
        walking: 'fa-walking',
        bicycling: 'fa-bicycle',
        transit: 'fa-bus'
    };
    transportIcon.className = `fas ${modeIcons[travelMode]} text-2xl mr-2`;

    modal.classList.remove('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
    const getItineraryBtn = document.getElementById('getItinerary');
    getItineraryBtn.addEventListener('click', async () => {
        const fromCampusName = document.getElementById('fromCampus').value;
        const toCampusName = document.getElementById('toCampus').value;
        const travelMode = document.getElementById('travelMode').value;

        if (fromCampusName && toCampusName && travelMode) {
            let fromLat, fromLng;
            if (fromCampusName === 'userLocation') {
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(async (position) => {
                        fromLat = position.coords.latitude;
                        fromLng = position.coords.longitude;
                        await calculateItinerary(fromLat, fromLng, toCampusName, travelMode);
                    });
                } else {
                    console.error('Geolocation is not supported by this browser.');
                }
            } else {
                const fromCampus = await fetch(`/api/campuses/byName?name=${fromCampusName}`).then(res => res.json());
                fromLat = fromCampus.latitude;
                fromLng = fromCampus.longitude;
                await calculateItinerary(fromLat, fromLng, toCampusName, travelMode);
            }

            // Clear existing markers
            markers.forEach(marker => marker.setMap(null));
            markers = [];
        } else {
            console.error('Please select both campuses and a travel mode.');
        }
    });

    async function calculateItinerary(fromLat, fromLng, toCampusName, travelMode) {
        const toCampus = await fetch(`/api/campuses/byName?name=${toCampusName}`).then(res => res.json());
        const data = await getItinerary(fromLat, fromLng, toCampus.latitude, toCampus.longitude, travelMode);
        console.log(data);

        const request = {
            origin: { lat: fromLat, lng: fromLng },
            destination: { lat: toCampus.latitude, lng: toCampus.longitude },
            travelMode: travelMode.toUpperCase()
        };

        directionsService.route(request, (result, status) => {
            if (status === 'OK') {
                directionsRenderer.setDirections(result);
                const route = result.routes[0].legs[0];
                showModal(route.distance.text, route.duration.text, travelMode, route.start_address, route.end_address);
            } else {
                console.error('Directions request failed due to ' + status);
            }
        });

        // Switch to map view
        document.getElementById('listView').classList.add('hidden');
        document.getElementById('mapView').classList.remove('hidden');
    }

    const mapViewBtn = document.getElementById('mapViewBtn');
    const listViewBtn = document.getElementById('listViewBtn');
    const mapView = document.getElementById('mapView');
    const listView = document.getElementById('listView');
    const searchButton = document.getElementById('searchButton');
    const citySelect = document.getElementById('citySelect');

    const fromCampus = document.getElementById('fromCampus');
    const toCampus = document.getElementById('toCampus');

    fromCampus.addEventListener('change', () => {
        const selectedCampus = fromCampus.value;
        const options = toCampus.querySelectorAll('option');
        options.forEach(option => {
            if (option.value === selectedCampus) {
                option.style.display = 'none';
            } else {
                option.style.display = 'block';
            }
        });
    });

    mapViewBtn.addEventListener('click', () => {
        mapView.classList.remove('hidden');
        listView.classList.add('hidden');
    });

    listViewBtn.addEventListener('click', () => {
        listView.classList.remove('hidden');
        mapView.classList.add('hidden');

        // Hide the itinerary modal
        document.getElementById('itineraryModal').classList.add('hidden');

        // Add markers back to the map
        fetchCampuses().then(campuses => {
            addMarkers(campuses);
        });
    });

    searchButton.addEventListener('click', () => {
        const selectedCity = citySelect.value;
        const itineraryModal = document.getElementById('itineraryModal');

        // Hide the modal
        itineraryModal.classList.add('hidden');

        if (selectedCity) {
            fetchCampuses(`/byCity?city=${selectedCity}`).then(campuses => {
                renderCampusList(campuses);
                addMarkers(campuses);

                // Remove the itinerary
                directionsRenderer.set('directions', null);

                // Move the map to the selected city
                if (campuses.length > 0) {
                    map.setCenter({ lat: campuses[0].latitude, lng: campuses[0].longitude });
                    map.setZoom(12);
                }
            });
        } else {
            fetchCampuses().then(campuses => {
                renderCampusList(campuses);
                addMarkers(campuses);

                // Remove the itinerary
                directionsRenderer.set('directions', null);
            });
        }
    });

    // Close modal event
    document.getElementById('closeModal').addEventListener('click', () => {
        document.getElementById('itineraryModal').classList.add('hidden');
    });

    function loadGoogleMapsApi() {
        const script = document.createElement('script');
        script.src = `https://maps.googleapis.com/maps/api/js?key=${googleApiKey}&callback=initMap`;
        script.async = true;
        script.defer = true;
        document.head.appendChild(script);
    }

    loadGoogleMapsApi();
});