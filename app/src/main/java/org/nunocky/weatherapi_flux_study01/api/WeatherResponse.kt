package org.nunocky.weatherapi_flux_study01.api

class WeatherResponse {
    var pinPointLocations: List<PinPointLocation> = arrayListOf()
    var link: String = ""
    var forecasts: List<Forecast> = arrayListOf()
    var location = Location()
    var publicTime = ""
    var copyright = Copyright()
    var title: String = ""
    var description = Description()

    class PinPointLocation {
        var link = ""
        var name = ""
    }

    class Forecast {
        var dateLavel = ""
        var telop = ""
        var temperature = Temperature()
        var image = Image()
    }

    class Temperature {
        var min = TemperatureSub()
        var max = TemperatureSub()

        class TemperatureSub {
            var celsius = ""
            var fahrenheit = ""
        }
    }

    class Image {
        var width = 0
        var url = ""
        var title = ""
        var height = 0
    }

    class Location {
        var city: String? = null
        var area: String? = null
        var prefecture: String? = null
    }

    class Copyright {
        var provider: List<Provider> = arrayListOf()
        var link = ""
        var title = ""
        var image = Image()

        class Provider {
            var link = ""
            var name = ""
        }
    }

    class Description {
        var text = ""
        var publicTime = ""
    }
}