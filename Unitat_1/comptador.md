# Programació multimèdia i dispositius mòbils
### Pràctica 1. Completem la App Comptador vist al punt 5.
#### Alumne: Jesús Olmos  
---
---

#### 1. Anàlisi de l'estructura del projecte

EL projecte que tractem en aquesta unitat-activitat es un comptador que al iniciar (ja amb totes les funcionalitats) li dona l'opció a l'ususari de premer 3 botons definits a la vista, un que resta el valor (inicialment es 0), un que reinicia el valor al estat inicial, i un botó que incrementa el seu valor. Tant el increment com el decrement va de 1 en 1.
En aquest procés es solucionen certs errors de canvis d'estat per a que l'usuari note un procés fluit si rota la pantalla, si deixa pausada l'aplicació, etc...
Com a fitxers més importants en un primer moment podem veure els layouts (vistes amb la definició del que veurà l'usuari), classes on estàn els events per a que l'usuari interactue amb la vista i el fitxer Manifest.xml on està la configuració de l'aplicació i la definició de les activitats.

>>>(Aquest projecte te a més a més un botó que obri una finestra amb el valor del comptador i té un botó per a tancar-la i tornar a la pantalla (activitat) inicial).

Els fitxers implicats en l'estructura d'una Activitat son:
- Fitxer Clase Kotlin:
    - Conté la classe de l'Activitat i inicialment deurem de programar el mètode onCreate. Podem necessitar altres events del cicle de vida o guardar estats per a poder recuperar-los més endavant.
- Vista XML (layout):
    - Es l'estructura que veurà l'usuari final i es trova al directori res/layout. En aquest fitxer deguem implementar el disseny de components que l'usuari visualitzarà i podrà (si ho te la classe) interactuar en ells (amb events).
- AndroidManifest:
    - Configuració per a què el dispositiu en reconega l'estructura i certes especificacions. En aquest fitxer tenim la definició de les Activitats creades.
- Directori res:
Fitxers de recursos:
    - Podrem tindre imatges, animacions, estils que es necessiten en tots els layouts.
    

Anem a intentar resoldre aquesta pregunta: 
>>>"Si volguera afegir una nova activitat, sería suficient crear el fitxer de layout i el fitxer Kotlin amb la classe?"  

En el cas que necessitarem afegir una nova activitat no sols hem de crear la vista i la clase, sino que hem d'afegir-ho també al fitxer Manifest.xml ja que sense aquest pas el projecte-aplicació no sabrà que existeix una "vista" nova que hem creat.
A més a més podem configurar com s'obrirà aquesta nova activitat.

>>>Nota: Android Studio ens dona l'opció de creár l'Activitat amb un assistent, d'aquesta manera quan sel·leccionem l'activitat que volem ell inclou, la classe, el layout i inclou l'activitat al Manifest.xml

---  
---  

#### 2. Anàlisi del cicle de vida i el problema de la pèrdua d'estat

Per a saber els cicles de vida i el seu ordre hem creat un sistema de Logs per a que ens vaja dient per on pasa en cada moment. Per a facilitar-nos la feïna hem ficat el mateix TAG d'aquesta manera al filtrar els registres que va generant l'aplicació podrem cercar-los més rapidament. 

```sh
override fun onStart() {
    super.onStart()
    Log.i("AVIS", "Entrem per Start")
}

override fun onResume() {
    super.onResume()
    Log.i("AVIS", "Entrem per Resume")
}

override fun onPause() {
    super.onPause()
    Log.i("AVIS", "Entrem per Pause")
}

override fun onRestart() {
    super.onRestart()
    Log.i("AVIS", "Entrem per Restart")
}

override fun onStop() {
    super.onStop()
    Log.i("AVIS", "Entrem per Stop")
}

override fun onDestroy() {
    super.onDestroy()
    Log.i("AVIS", "Entrem per Destroy")
}
```

El problema el tenim que al rotar la pantalla passem per aquests cicles de vida:
- Entrem per Stop
- Entrem per Destroy
- Entrem per Start
- Entrem per Resume

i la variable comptador per el seu valor al anar rotant-la ja que l'activitat principal s'atura

El mateix passa al obrir una altra app, anar al inici del nostre dispositiu etc.
- Entrem per Pause
- Entrem per Stop
- Entrem per Restart
- Entrem per Start
- Entrem per Resume

Aleshores al fer sempre un "reinici" del cicle de vida anem perdent sempre el valor que teniem en l'ultim moment.

---
---
#### 3. Solució a la pèrdua d'estat

Una vegada em vist el cicle de vida i hem revisat l`ordre per el qual va encaminant-se podem resoldre aquest error utilitzant els mètodes onSaveInstanceState(estat: Bundle) i onRestoreInstanceState(estat: Bundle).

Abans de que s'execute onDestroy hem de guardar l'estat. 
```ssh
override fun onSaveInstanceState(estat: Bundle) {
    super.onSaveInstanceState(estat)
    // Codi per a guardar l'estat
    estat.putInt("COMPTADOR", comptador)
}
```
Com veïem és una sintaxi molt pareguda al que podem utilitzar a les aplicacions web quan guardem al localStorage, ja que necessitem un objecte composat per una clau i un valor.

Seguidament quan es fagen canvis com la rotació de la pantalla o es torne a l'aplicació abans que s'execute onResume hem de recuperar aquest valor i tornar a assignar-li'l a la variable. A més a més també farem que mostre per pantalla el valor que tenia.

```ssh
override fun onRestoreInstanceState(estat: Bundle) {
    super.onRestoreInstanceState(estat)
    // Codi per a guardar l'estat
    comptador = estat.getInt("COMPTADOR")
    // Referencia al TextView
    //findViewById<TextView>(R.id.textViewComptador).text = comptador.toString()
    // Amb DataBinding
    binding.textViewComptador.text = comptador.toString()
}
```
Amb aquestes modificacions al còdi podrem rotar, pausar o eixir de la aplicació (sense tancar-la desde el dispositiu) i mantindrem el estat del valor de comptador.

---
---

#### 4. Ampliant la funcionalitat amb decrements i resets

A l'activitat de l'aplicació principal (MainActivity) hem afegit dos events per als dos botons nous (restar) i initcializar (o reset).
Com hem fet l'activitat tant per a accedir utilitzant el findViewById com per a dataBinding en aquest apart sols mostrarem la funcionalitat del findViewById (ja que en el pròxim punt especificarem la nova funcionalitat).

El primer que necessitem és cercar els elements de la vista els quals necessitem afegir, en aquest cas, un event. Tant per a la resta com per al reinici afegirem l'event click, ja que necessitarem llançar un event quan el usuari fa aquest acció (click o tap al mòbil) i realitzar un canvi en el valor que estem mostrant.
Per a cercar el element de la vista utilitzarem findViewByID i entre <> especificarem l'etiqueta o quin component és, en aquest cas els dos son Buttons.
Una vegada em especificat el element hem de especificar quin id te per això la instrucció que segueix R.id.<<Nom_del_id>>

Per últim el que volem fer, en el cas de restar, restarem 1 al contador i mostrarem el resultat. En el cas de resetejar ficarem el valor a 0 i el mostrarem. Ací tenim el còdi al que fa referència:
```ssh
val btSubs = findViewById<Button>(R.id.btSubtract)
btSubs.setOnClickListener{
    comptador--;
    textViewContador.text=comptador.toString()
}

val btRes = findViewById<Button>(R.id.btReset)
btRes.setOnClickListener {
    comptador = 0
    textViewContador.text = comptador.toString()
}
```

---
---

#### 5. Aplica el View Binding

Per a aplicar el ViewBinding o vinculació de vistes hem cercat a diferents webs una d'elles la de android developers.
Per a poder utilitzar-ho hem de primer configurar les buildFeatures al Gradle del mòdul de l'aplicació.
Dins del gradle a l'objecte android afegirem
>>> buildFeatures {
>>>  viewBinding = true
>>> }

Açò provocarà que sincronitzem el projecte per a veure els canvis (potser com ha sigut el meu cas que es demore uns minuts).
Una vegada ha acavat el que farem serà importar la classe al MainActivity.
>>import com.pmdm.ieseljust.comptador.databinding.ActivityMainBinding
L'ultima part (ActivityMainBinding) te relació amb el nom del fitxer xml tot junt i afegint Binding (utilitzant PascalCase).

Ara a la classe MainActivity podrem initcialitzar la variable del binding (la que farà referència a la classe importada). Utilitzarem el nom de variable lateinit a binding

>>> private lateinit var binding: ActivityMainBinding

Ara dins del mètode onCreate utilitzarem el l'unflat a la vista
```ssh
binding = ActivityMainBinding.inflate(layoutInflater)
val view = binding.root
setContentView(view)
```

Ara modificarem tots els event creats i les accion per a recuperar el elements de la vista

```ssh
// Inicialitzem el TextView amb el comptador a 0
binding.textViewComptador.text = comptador.toString()

// Configurar los listeners de los botones
binding.btAdd.setOnClickListener {
    comptador += 1
    binding.textViewComptador.text = comptador.toString()
}

binding.btSubtract.setOnClickListener {
    comptador -= 1
    binding.textViewComptador.text = comptador.toString()
}

binding.btReset.setOnClickListener {
    comptador = 0
    binding.textViewComptador.text = comptador.toString()
}

binding.btOpen.setOnClickListener {
    Intent(baseContext, MostraComptadorActivity::class.java).apply {
        putExtra("comptador", comptador)
        startActivity(this)
    }
}
```

També haurem de modificar el xml i fer que penje de un layout i seguir utilitzant els ids de la vista xml per a poder accedir.

Amb tot açò tenim reconvertit el projecte de comptador a view bindings.

---
---
## Bibliografia
Pàgines web cercades per a traure informació i ajuda en la resolució i compresió de certs punts de l'activitat. (webs externes a aules i material del docent).

| WEB | URL |
| --------- | --------- |
| Android Developers | [Vinculación de vistas][AndDev] |
| Hack Aprende | [DataBinding Kotlin][HackAp] |

[//]: # (URLS WEBS)
[AndDev]: <https://developer.android.com/topic/libraries/view-binding?hl=es-419#kotlin>
[HackAp]: <https://hackaprende.com/2020/07/23/data-binding-para-android-con-kotlin/>