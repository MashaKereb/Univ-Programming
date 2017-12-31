// import {Scene, PerspectiveCamera, BoxGeometry, WebGLRenderer, MeshBasicMaterial, Mesh} from "three"
// import * as THREE from "three"
import OrbitControls from "three-orbitcontrols"

var THREE = require('three');
var STLLoader = require('three-stl-loader')(THREE);

class Graphics {
    constructor() {
        this.tick = 0;

        let wsUri = "ws://" + window.location.hostname + ":" + window.location.port + "/connect/";
        console.log(wsUri);
        this.websocket = new WebSocket(wsUri);
        this.websocket.onerror = this.onError.bind(this);
        this.websocket.onopen = this.onOpen.bind(this);
        this.websocket.onmessage = this.onMessage.bind(this);

        this.scene = new THREE.Scene();
        this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 1, 1000);
        this.camera.position.z = 60;
        this.renderer = new THREE.WebGLRenderer();
        this.renderer.setSize(window.innerWidth, window.innerHeight);
        this.renderer.setClearColor(0xAEEDF3);

        this.renderer.shadowMap.enabled = true;
        this.renderer.toneMapping = THREE.ReinhardToneMapping;
        this.start = false;
        new STLLoader().load('model (3).stl', this.onLoadCannon.bind(this));

        document.body.appendChild(this.renderer.domElement);


        let sphereGeom = new THREE.SphereGeometry(4, 16, 8);
        let material = new THREE.MeshStandardMaterial({
            roughness: 0.7,
            color: 0xffffff,
            bumpScale: 0.002,
            metalness: 0.2
        });

        this.sphereMesh = new THREE.Mesh(sphereGeom, material);


        let hemiLight = new THREE.HemisphereLight(0xddeeff, 0x0f0e0d, 0.2);

        this.scene.add(hemiLight);

        let bulbGeometry = new THREE.SphereGeometry(0.02, 16, 8);
        let bulbMat = new THREE.MeshStandardMaterial({
            emissive: 0xffffee,
            emissiveIntensity: 1,
            color: 0xff0000
        });
        this.light = new THREE.PointLight(0xffee88, 1, 2000, 2);
        this.light.add(new THREE.Mesh(bulbGeometry, bulbMat));
        this.light.position.y = 125;
        this.scene.add(this.light);

        let floorMat = new THREE.MeshStandardMaterial({
            roughness: 0.8,
            color: 0x1D621D,
            metalness: 0.2,
            bumpScale: 0.0005
        });

        let floorGeometry = new THREE.PlaneBufferGeometry(10000, 10000);
        let floorMesh = new THREE.Mesh(floorGeometry, floorMat);
        floorMesh.receiveShadow = true;
        floorMesh.rotation.x = -Math.PI / 2.0;
        floorMesh.position.y = -8;
        this.scene.add(floorMesh);


        let controls = new OrbitControls(this.camera, this.renderer.domElement);
        controls.target.set(0, 0, 0);
        controls.update();


        this.animate();

    }


    onLoadCannon(geometry) {
        let material = new THREE.MeshStandardMaterial({
            roughness: 0.7,
            color: 0xffffff,
            bumpScale: 0.002,
            metalness: 0.2
        });
        this.cannon = new THREE.Mesh(geometry, material);

        this.cannon.position.set(0, 0, 0);
        this.cannon.rotation.set(Math.PI, 0, 0);

        this.cannon.castShadow = true;
        this.scene.add(this.cannon);
        this.rotationSlider = new RotationSlider(this.cannon);
        this.startButton = new StartButton(this.rotationSlider, this.websocket);
    }

    animate() {
        requestAnimationFrame(this.animate.bind(this));

        if (this.start && this.one_by_one) {
            this.sphereMesh.position.x = this.data.trajectoryPoint.x;
            this.sphereMesh.position.y = this.data.trajectoryPoint.y;
            this.sphereMesh.position.z = this.data.trajectoryPoint.z;
        } else if(this.start && this.tick < this.data.trajectory.length) {
            this.sphereMesh.position.x = this.data.trajectory[this.tick].x;
            this.sphereMesh.position.y = this.data.trajectory[this.tick].y;
            this.sphereMesh.position.z = this.data.trajectory[this.tick].z;
            this.tick++;
        }
        this.renderer.render(this.scene, this.camera);

    }

    onError(evt) {
        console.log(evt.data);
    }


    onOpen() {
        console.log("connected");
    }

    onMessage(evt) {
        let obj = JSON.parse(evt.data);
        let data;
        switch (obj.type) {
            case "next_response":
                data = JSON.parse(obj.data);
                if(!this.start){
                    this.one_by_one = true;
                    this.sphereMesh.position.x = data.trajectoryPoint.x;
                    this.sphereMesh.position.y = data.trajectoryPoint.y;
                    this.scene.add(this.sphereMesh);
                }
                this.start = true;
                this.data = data;


                break;
            case "start_response":
                data = JSON.parse(obj.data);
                this.sphereMesh.position.x = data.trajectory[0].x;
                this.sphereMesh.position.y = data.trajectory[0].y;
                this.scene.add(this.sphereMesh);
                this.start = true;
                this.data = data;
                this.tick = 0;
                break;



        }
    }
}

class RotationSlider {
    constructor(cannon) {
        this.cannon = cannon;
        this.rotationSlider = document.querySelector("#rotation");
        this.rotationSlider.addEventListener("input", this.onChange.bind(this));
        this.rotationSlider.value = 0;
    }

    onChange() {
        this.cannon.rotation.z = this.rotationSlider.value;
    }

    getValue() {
        return this.rotationSlider.value;
    }
}

class StartButton {
    constructor(rotationSlider, websocket) {
        this.websocket = websocket;
        this.rotationSlider = rotationSlider;
        this.forceSlider = document.querySelector("#force");
        this.startButton = document.querySelector("#start");
        this.startButton.addEventListener("click", this.onClick.bind(this));
    }

    onClick() {
        let startData = {
            angle: this.rotationSlider.getValue(),
            surfaceY: -8,
            barrelRadius: 4,
            force: this.forceSlider.value,
            barrelHeight: 18,
            positionX: 0,
            positionY: 0
        };
        let message = {
            type: "PARTIAL",
            data: JSON.stringify(startData)
        };
        this.websocket.send(JSON.stringify(message));
    }
}

export {Graphics};