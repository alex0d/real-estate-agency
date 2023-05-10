var script = document.createElement('script');
script.src = `https://maps.googleapis.com/maps/api/js?key=AIzaSyB41DRUbKWJHPxaFjMAwdrzWzbVKartNGg&callback=initMap`;
script.async = true;

window.initMap = function() {
    var centerLatitude = 55.754093;
    var centerLongitude = 37.620407;

    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 13,
        center: { lat: centerLatitude, lng: centerLongitude },
    });

    let cur_q = document.getElementById("current-query")?.textContent || "";
    fetch(`/api/real-estate?q=${cur_q}`)
        .then((response) => response.json())
        .then((data) => {
            data.forEach((realEstate) => {
                var marker = new google.maps.Marker({
                    position: { lat: realEstate.latitude, lng: realEstate.longitude },
                    map: map,
                    title: realEstate.name,
                });

                var infoWindow = new google.maps.InfoWindow({
                    content: `<a href="/real-estate/${realEstate.id}">${realEstate.name}</a><br>${realEstate.address}<br>${realEstate.price} &#8381;`
                });

                marker.addListener("click", () => {
                    infoWindow.open(map, marker);
                });
            });
        });
};

document.head.appendChild(script);
