package org.nunocky.weatherapi_flux_study01.api

class WeatherResponse {
    var pinPointLocations: List<PinPointLocation>? = null
    var link: String? = null
    var forecasts: List<Forecast>? = null
    var location: Location? = null
    var publicTime: String? = null
    var copyright: Copyright? = null
    var title: String? = null
    var description: Description? = null

    class PinPointLocation {
        var link: String? = null
        var name: String? = null
    }

    class Forecast {
        var dateLavel: String? = null
        var telop: String? = null
        var temperature: Temperature? = null
        var image: Image? = null
    }

    class Temperature {
        var min: TemperatureSub? = null
        var max: TemperatureSub? = null

        class TemperatureSub {
            var celsius: String? = null
            var fahrenheit: String? = null
        }
    }

    class Image {
        var width = 0
        var url: String? = null
        var title: String? = null
        var height = 0
    }

    class Location {
        var city: String? = null
        var area: String? = null
        var prefecture: String? = null
    }

    class Copyright {
        var provider: List<Provider>? = null
        var link: String? = null
        var title: String? = null
        var image: Image? = null

        class Provider {
            var link: String? = null
            var name: String? = null
        }
    }

    class Description {
        var text: String? = null
        var publicTime: String? = null
    }
}