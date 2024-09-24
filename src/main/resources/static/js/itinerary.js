// src/main/resources/static/js/itinerary.js
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
});