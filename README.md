# 🤖 Automatización y Evaluación de Atributos de Calidad - OpenCart

Este proyecto consiste en un conjunto de pruebas automatizadas desarrolladas en **Java** utilizando **Selenium WebDriver**, con el objetivo de evaluar el sitio de demostración de OpenCart.

## 🌐 Sitio Web Objetivo

[https://opencart.abstracta.us/](https://opencart.abstracta.us/)

## 🚀 Tecnologías Utilizadas

- Java  
- Selenium WebDriver  
- Maven  
- Apache POI (para manejo de Excel)  
- Patrón de diseño Page Object Model (POM)  

## 🎯 Objetivo del Proyecto

Desarrollar un framework de automatización siguiendo buenas prácticas, que permita evaluar atributos de calidad como:

- Diseño estructurado de pruebas
- Selección eficiente de selectores
- Sincronización adecuada
- Validaciones precisas
- Manejo de datos externos mediante Excel


## 🧩 Requisitos Previos

- Java JDK 8+
- Maven
- Google Chrome (última versión)
- ChromeDriver (compatible con tu navegador)
- IDE (recomendado: IntelliJ IDEA o Eclipse)

## 📦 Instalación y Ejecución

1. **Instalacion**

```bash
git clone https://github.com/tu-usuario/nombre-del-repo.git
cd nombre-del-repo

mvn clean install

mvn test
```
2. **Commit a una rama**
```bash
# Verificar los cambios pendientes
git status

# Añadir todos los cambios
git add .

# Hacer commit con mensaje
git commit -m "[ADD O UPDATE O DELETE ]Descripción de los cambios"       //Dependera de que si insertas algo nuevo o una actualizacion de variables

# Subir la rama al repositorio remoto
git push origin nombre-de-tu-rama

# Actualizar tu rama con los últimos cambios de main
git pull origin nombre-de-la-rama-que-quieres-traer-los-cambios
```
