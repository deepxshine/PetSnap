package my.project.petsnap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetsnapApplication

fun main(args: Array<String>) {
	runApplication<PetsnapApplication>(*args)
}
