# Sistema de Inventario - MVC

Refactor del ejercicio 0 (una sola clase) a tres paquetes.

## Estructura
```
src/
  modelo/        Producto, Inventario, excepciones propias
  vista/         VistaConsola
  controlador/   ControladorInventario
  App.java       ensambla y arranca
```

## Compilar y ejecutar
```
mkdir out
javac -d out src/App.java src/modelo/*.java src/vista/*.java src/controlador/*.java
java -cp out App
```

## Dependencias (las flechas apuntan hacia el modelo)
```
App -----------> controlador -----------> modelo
 |                    |                      ^
 |                    +----> vista ----------+
 +--------------------------------------------^
```
- controlador -> modelo, controlador -> vista
- vista -> modelo (solo para recibir `Producto` y mostrarlo)
- modelo no importa nada de vista ni de controlador

## Nota
El comportamiento se conservó idéntico al original, incluidos dos detalles
heredados: vender una cantidad negativa aumenta el stock, y el valor total
se imprime sin formato de decimales.

## Pruebas (JUnit 4)
```
mkdir out
javac -d out src/modelo/*.java
javac -cp out:junit-4.x.jar:hamcrest-core.jar -d out test/modelo/InventarioTest.java
java  -cp out:junit-4.x.jar:hamcrest-core.jar org.junit.runner.JUnitCore modelo.InventarioTest
```

## Segunda vista (CSV)
```
java -cp out App        # vista de consola (igual que antes)
java -cp out App csv    # la opcion 3 exporta a inventario.csv
```
El controlador depende de la interfaz `vista.Vista`; el paquete `modelo` no cambio.
