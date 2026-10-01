const STORAGE_KEY="homeRepairDemo.v2";
const PROFESSIONS=["ALBAÑIL","CARPINTERO","CERRAJERO","ELECTRICISTA","GASISTA","HERRERO","LIMPIEZA","PLOMERO"];
const PROFESSION_META={
  "ALBAÑIL":{icon:"./img/cement.svg",label:"Albañilería"},
  "CARPINTERO":{icon:"./img/carpenter.svg",label:"Carpintería"},
  "CERRAJERO":{icon:"./img/locksmith.svg",label:"Cerrajería"},
  "ELECTRICISTA":{icon:"./img/electrician.svg",label:"Electricidad"},
  "GASISTA":{icon:"./img/gas.svg",label:"Instalación de gas"},
  "HERRERO":{icon:"./img/blacksmith.svg",label:"Herrería"},
  "LIMPIEZA":{icon:"./img/cleaning.svg",label:"Limpieza"},
  "PLOMERO":{icon:"./img/plumbing.svg",label:"Fontanería"}
};
const STATUS_LABELS={REQUIRED:"Solicitado",ACCEPTED:"Aceptado",DONE:"Finalizado",REVERT:"Cancelado",REVIEWD:"Valorado"};
const ROLE_LABELS={CUSTOMER:"Cliente",PROVIDER:"Profesional",ADMIN:"Administrador"};

const seed=()=>({
  currentUserId:null,
  users:[
    {id:"u-admin",name:"Federico",lastname:"Trucco",email:"admin@homerepair.demo",password:"demo123",role:"ADMIN",profession:null,phone:"",description:"Administración de Home Repair.",rating:0,active:true,image:"./img/profileImg.png"},
    {id:"u-customer",name:"Lucía",lastname:"Gómez",email:"cliente@homerepair.demo",password:"demo123",role:"CUSTOMER",profession:null,phone:"+34 611 204 410",description:"",rating:0,active:true,image:"./img/customer-avatar-blue.png"},
    {id:"u-c2",name:"Marina",lastname:"Soler",email:"marina@homerepair.demo",password:"demo123",role:"CUSTOMER",profession:null,phone:"+34 622 915 830",description:"",rating:0,active:true,image:"./img/customer-avatar-orange.png"},
    {id:"u-c3",name:"Álvaro",lastname:"Navarro",email:"alvaro@homerepair.demo",password:"demo123",role:"CUSTOMER",profession:null,phone:"+34 633 761 220",description:"",rating:0,active:true,image:"./img/customer-avatar-green.png"},
    {id:"u-p1",name:"Martín",lastname:"Pérez",email:"fontanero@homerepair.demo",password:"demo123",role:"PROVIDER",profession:"PLOMERO",phone:"+34 610 555 101",description:"Especialista en fugas, grifería, instalaciones y mantenimiento doméstico. Atención en Barcelona y alrededores.",rating:5,active:true,image:"./img/provider-avatar-lightblue.png"},
    {id:"u-p2",name:"Carla",lastname:"Ruiz",email:"electricista@homerepair.demo",password:"demo123",role:"PROVIDER",profession:"ELECTRICISTA",phone:"+34 610 555 102",description:"Instalaciones eléctricas, cuadros, iluminación y mantenimiento preventivo para viviendas y pequeños comercios.",rating:4,active:true,image:"./img/provider-avatar-violet.png"},
    {id:"u-p3",name:"Diego",lastname:"López",email:"carpintero@homerepair.demo",password:"demo123",role:"PROVIDER",profession:"CARPINTERO",phone:"+34 610 555 103",description:"Muebles a medida, reparación de puertas, restauración y soluciones de carpintería interior.",rating:5,active:true,image:"./img/provider-avatar-green.png"},
    {id:"u-p4",name:"Paula",lastname:"Méndez",email:"limpieza@homerepair.demo",password:"demo123",role:"PROVIDER",profession:"LIMPIEZA",phone:"+34 610 555 104",description:"Limpieza profunda, mantenimiento periódico y puesta a punto de pisos antes o después de una mudanza.",rating:5,active:true,image:"./img/provider-avatar-orange.png"},
    {id:"u-p5",name:"Sergio",lastname:"Romero",email:"cerrajero@homerepair.demo",password:"demo123",role:"PROVIDER",profession:"CERRAJERO",phone:"+34 610 555 105",description:"Aperturas, sustitución de cerraduras y refuerzo de accesos con atención rápida y presupuesto previo.",rating:4,active:true,image:"./img/provider-avatar-red.png"},
    {id:"u-p6",name:"Nuria",lastname:"Castro",email:"gasista@homerepair.demo",password:"demo123",role:"PROVIDER",profession:"GASISTA",phone:"+34 610 555 106",description:"Revisión de instalaciones, mantenimiento y pequeñas reparaciones domésticas con foco en seguridad.",rating:5,active:true,image:"./img/provider-avatar.png"}
  ],
  works:[
    {id:"w-1",customerId:"u-customer",providerId:"u-p1",name:"Fuga en cocina",description:"Hay una fuga de agua debajo del fregadero.",status:"REVIEWD",review:"Llegó puntual, encontró la fuga rápido y dejó todo limpio.",rating:5},
    {id:"w-2",customerId:"u-customer",providerId:"u-p2",name:"Cambio de luminaria",description:"Reemplazar una lámpara de techo.",status:"ACCEPTED",review:"",rating:0},
    {id:"w-3",customerId:"u-c2",providerId:"u-p1",name:"Cambio de grifo",description:"Sustitución de grifo de lavabo.",status:"REVIEWD",review:"Muy claro con el presupuesto y buen acabado.",rating:5},
    {id:"w-4",customerId:"u-c3",providerId:"u-p2",name:"Revisión de cuadro eléctrico",description:"Saltaba el diferencial de forma intermitente.",status:"REVIEWD",review:"Buena explicación del problema y solución ordenada.",rating:4},
    {id:"w-5",customerId:"u-c2",providerId:"u-p3",name:"Ajuste de puerta",description:"La puerta rozaba el suelo y no cerraba bien.",status:"REVIEWD",review:"Trabajo fino y rápido. La puerta quedó perfecta.",rating:5},
    {id:"w-6",customerId:"u-customer",providerId:"u-p4",name:"Limpieza de fin de alquiler",description:"Limpieza completa de un piso de dos habitaciones.",status:"REVIEWD",review:"Muy organizada y detallista. El piso quedó impecable.",rating:5}
  ]
});

const store={
  load(){
    try{
      const raw=localStorage.getItem(STORAGE_KEY);
      if(!raw){const initial=seed();this.save(initial);return initial}
      const parsed=JSON.parse(raw);
      if(!parsed?.users||!parsed?.works)throw new Error("invalid");
      return parsed
    }catch{const initial=seed();this.save(initial);return initial}
  },
  save(state){localStorage.setItem(STORAGE_KEY,JSON.stringify(state))},
  reset(){const initial=seed();this.save(initial);return initial}
};

let state=store.load();
const app=document.querySelector("#app");
const header=document.querySelector("#site-header");
const toastEl=document.querySelector("#toast");
const currentUser=()=>state.users.find(u=>u.id===state.currentUserId&&u.active)??null;
const userById=id=>state.users.find(u=>u.id===id);
const escapeHtml=value=>String(value??"").replace(/[&<>"']/g,ch=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#039;"}[ch]));
const titleCase=value=>String(value??"").toLowerCase().replace(/(^|\s)\S/g,s=>s.toUpperCase());
const stars=rating=>"★".repeat(Math.max(0,Math.min(5,Number(rating)||0)))+"☆".repeat(5-Math.max(0,Math.min(5,Number(rating)||0)));
const reviewCount=providerId=>state.works.filter(w=>w.providerId===providerId&&w.status==="REVIEWD"&&w.rating>0).length;
const professionLabel=value=>PROFESSION_META[value]?.label??titleCase(value);
const uid=prefix=>`${prefix}-${Date.now().toString(36)}-${Math.random().toString(36).slice(2,7)}`;
const persist=()=>store.save(state);
const allowedWorkTransition=(actor,work,next)=>{
  if(!actor||!work)return false;
  const ownsCustomer=actor.role==="CUSTOMER"&&work.customerId===actor.id;
  const ownsProvider=actor.role==="PROVIDER"&&work.providerId===actor.id;
  if(actor.role==="ADMIN")return ["REVERT","ACCEPTED","DONE"].includes(next);
  if(work.status==="REQUIRED"&&next==="ACCEPTED")return ownsProvider;
  if(work.status==="REQUIRED"&&next==="REVERT")return ownsCustomer||ownsProvider;
  if(work.status==="ACCEPTED"&&["DONE","REVERT"].includes(next))return ownsCustomer||ownsProvider;
  return false;
};
const go=target=>{
  const next=`#${target}`;
  if(location.hash===next){route();return}
  location.hash=target;
};
const toast=message=>{toastEl.textContent=message;toastEl.classList.add("show");setTimeout(()=>toastEl.classList.remove("show"),2400)};

async function imageFileToAvatar(file){
  if(!file?.type?.startsWith("image/")) throw new Error("Selecciona una imagen válida.");
  if(file.size>5_000_000) throw new Error("La imagen no puede superar 5 MB.");

  const bitmap=await createImageBitmap(file);
  const side=Math.min(bitmap.width,bitmap.height);
  const sx=(bitmap.width-side)/2,sy=(bitmap.height-side)/2;
  const canvas=document.createElement("canvas");
  canvas.width=320;canvas.height=320;
  canvas.getContext("2d").drawImage(bitmap,sx,sy,side,side,0,0,320,320);
  bitmap.close?.();
  return canvas.toDataURL("image/jpeg",.82);
}

function nav(){
  const user=currentUser();
  header.innerHTML=`<nav class="nav" aria-label="Navegación principal"><div class="nav-inner">
    <a class="brand" href="#home">Home Repair</a>
    <div class="nav-links">
      <a href="#providers">Profesionales</a>
      ${user?'<a href="#works">Trabajos</a>':""}
      ${user?.role==="ADMIN"?'<a href="#admin">Administración</a>':""}
      <a href="#about">Acerca de</a>
      ${user?'<a href="#profile/me">Mi perfil</a><button class="link-button" data-action="logout">Salir</button>':'<a href="#login">Ingresar</a><a href="#register">Registrarse</a>'}
    </div></div></nav>`;
}

function layout(html){nav();app.innerHTML=`<div class="container">${html}</div>`;app.focus({preventScroll:true})}
function requireUser(){if(!currentUser()){go("login");return false}return true}
function requireRole(role){const user=currentUser();if(!user||user.role!==role){go("home");return false}return true}

function home(){
  const user=currentUser();
  const providers=state.users.filter(u=>u.role==="PROVIDER"&&u.active);
  const reviewed=state.works.filter(w=>w.status==="REVIEWD").length;
  layout(`<section class="hero">
    <div>
      <span class="eyebrow">Profesionales de confianza para tu hogar</span>
      <h1>Home Repair</h1>
      <p>${user?`Hola ${escapeHtml(user.name)}. Gestiona solicitudes, trabajos y valoraciones desde una experiencia completa de Home Repair.`:"Encuentra profesionales por especialidad, revisa perfiles y valoraciones, y gestiona todo el trabajo desde la solicitud hasta la calificación final."}</p>
      <div class="actions">
        <a class="btn" href="#providers">Explorar profesionales</a>
        ${user?'<a class="btn secondary" href="#works">Ver mis trabajos</a>':'<a class="btn secondary" href="#register">Crear cuenta</a>'}
      </div>
      <div class="stats">
        <div class="stat"><strong>${providers.length}</strong><span>profesionales demo</span></div>
        <div class="stat"><strong>${PROFESSIONS.length}</strong><span>especialidades</span></div>
        <div class="stat"><strong>${reviewed}</strong><span>valoraciones publicadas</span></div>
      </div>
    </div>
    <div class="hero-panel"><img class="hero-art" src="./img/homeRepair.svg" alt="Ilustración de reparaciones del hogar"></div>
  </section>

  <section class="section">
    <div class="section-header section-header-centered"><div><span class="eyebrow">Servicios</span><h2 class="section-title">¿Qué necesitas resolver?</h2><p class="section-copy">Filtra por especialidad y compara perfiles antes de solicitar un trabajo.</p></div></div>
    <div class="service-grid">${PROFESSIONS.map(p=>`<button class="service-card" data-profession="${p}"><img src="${PROFESSION_META[p].icon}" alt=""><span><strong>${PROFESSION_META[p].label}</strong><span>Ver profesionales</span></span></button>`).join("")}</div>
  </section>

  <section class="section">
    <div class="section-header section-header-centered"><div><span class="eyebrow">Destacados</span><h2 class="section-title">Profesionales mejor valorados</h2><p class="section-copy">Perfiles con experiencia, información visible y valoraciones de clientes.</p></div><a class="btn secondary small section-header-action" href="#providers">Ver todos</a></div>
    <div class="grid">${providers.sort((a,b)=>b.rating-a.rating).slice(0,3).map(providerCard).join("")}</div>
  </section>

  <section class="section work-zone" aria-labelledby="work-zone-title">
    <div class="section-header section-header-centered"><div><span class="eyebrow">Cobertura</span><h2 id="work-zone-title" class="section-title">Zona de trabajo</h2><p class="section-copy">Servicios disponibles en Barcelona y su área metropolitana.</p></div></div>
    <div class="map-card">
      <iframe src="https://www.google.com/maps?q=Barcelona%2C%20Espa%C3%B1a&z=11&output=embed" title="Mapa de zona de trabajo de Home Repair en Barcelona" loading="lazy" referrerpolicy="no-referrer-when-downgrade" allowfullscreen></iframe>
    </div>
  </section>

  <div class="notice"><strong>Demo pública:</strong> los datos y permisos se simulan en el navegador para poder recorrer el producto sin backend. La aplicación Java original conserva la implementación servidor.</div>`);
}

function providers(){
  const active=state.users.filter(u=>u.role==="PROVIDER"&&u.active);
  layout(`<h1 class="page-title">Encuentra profesionales</h1>
  <p class="meta">Busca por profesión, nombre o correo electrónico.</p>
  <div class="filters">
    <select id="profession-filter" aria-label="Filtrar por profesión"><option value="">Todas las profesiones</option>${PROFESSIONS.map(p=>`<option value="${p}">${professionLabel(p)}</option>`).join("")}</select>
    <input id="provider-search" type="search" placeholder="Nombre, apellido o correo electrónico" aria-label="Buscar profesional">
    <button class="btn" id="clear-search">Limpiar</button>
  </div>
  <div id="provider-grid" class="grid"></div>`);
  const render=()=>{
    const profession=document.querySelector("#profession-filter").value;
    const q=document.querySelector("#provider-search").value.trim().toLowerCase();
    const filtered=active.filter(u=>(!profession||u.profession===profession)&&(!q||[u.name,u.lastname,u.email,u.profession].some(v=>String(v??"").toLowerCase().includes(q))));
    document.querySelector("#provider-grid").innerHTML=filtered.length?filtered.map(providerCard).join(""):'<div class="empty">No encontramos profesionales con esos filtros.</div>';
  };
  document.querySelector("#profession-filter").addEventListener("change",render);
  document.querySelector("#provider-search").addEventListener("input",render);
  document.querySelector("#clear-search").addEventListener("click",()=>{document.querySelector("#profession-filter").value="";document.querySelector("#provider-search").value="";render()});
  render();
}
function providerCard(u){const count=reviewCount(u.id);return `<article class="card provider-card">
  <div class="provider-visual"><img class="card-avatar" src="${escapeHtml(u.image)}" alt="Foto de ${escapeHtml(u.name)} ${escapeHtml(u.lastname)}"></div>
  <div class="provider-body">
    <span class="badge">${professionLabel(u.profession)}</span>
    <h3>${escapeHtml(u.name)} ${escapeHtml(u.lastname)}</h3>
    <div class="rating-line"><span class="rating" aria-label="${u.rating} de 5 estrellas">${stars(u.rating)}</span><span class="rating-number">${u.rating.toFixed?.(1)??u.rating}</span><span class="review-count">${count} ${count===1?"valoración":"valoraciones"}</span></div>
    <p>${escapeHtml(u.description)}</p>
    <a class="btn small" href="#profile/${u.id}">Ver perfil</a>
  </div>
</article>`}

function login(){
  layout(`<div class="form-card card"><h1 class="page-title">Ingresar</h1>
    <p>Puedes usar una cuenta demo o los datos creados en este navegador.</p>
    <div class="demo-users">
      <button class="demo-user" data-demo="u-customer"><img src="./img/customer-avatar-blue.png" alt=""><span><strong>Cliente</strong><span>cliente@homerepair.demo</span></span></button>
      <button class="demo-user" data-demo="u-p1"><img src="./img/provider-avatar-lightblue.png" alt=""><span><strong>Profesional</strong><span>fontanero@homerepair.demo</span></span></button>
      <button class="demo-user" data-demo="u-admin"><img src="./img/profileImg.png" alt=""><span><strong>Administrador</strong><span>admin@homerepair.demo</span></span></button>
    </div>
    <form id="login-form">
      <div class="field"><label for="email">Correo electrónico</label><input id="email" name="email" type="email" autocomplete="email" required></div>
      <div class="field"><label for="password">Contraseña</label><input id="password" name="password" type="password" autocomplete="current-password" required></div>
      <p id="login-error" class="form-error" role="alert"></p>
      <button class="btn" type="submit">Ingresar</button>
    </form>
    <p class="meta">Contraseña de las cuentas demo: <strong>demo123</strong></p>
  </div>`);
  document.querySelectorAll("[data-demo]").forEach(btn=>btn.addEventListener("click",()=>{state.currentUserId=btn.dataset.demo;persist();toast("Sesión demo iniciada");go("home")}));
  document.querySelector("#login-form").addEventListener("submit",event=>{
    event.preventDefault();const data=new FormData(event.currentTarget);
    const user=state.users.find(u=>u.active&&u.email.toLowerCase()===String(data.get("email")).toLowerCase()&&u.password===data.get("password"));
    if(!user){document.querySelector("#login-error").textContent="Correo electrónico o contraseña incorrectos.";return}
    state.currentUserId=user.id;persist();go("home");
  });
}

function register(){
  layout(`<div class="form-card card"><h1 class="page-title">Regístrate</h1>
    <form id="register-form">
      <div class="field"><label for="role">Quiero</label><select id="role" name="role"><option value="CUSTOMER">Contratar servicios</option><option value="PROVIDER">Ofrecer servicios</option></select></div>
      <div class="field"><label for="name">Nombre</label><input id="name" name="name" required maxlength="60"></div>
      <div class="field"><label for="lastname">Apellido</label><input id="lastname" name="lastname" required maxlength="60"></div>
      <div class="field"><label for="reg-email">Correo electrónico</label><input id="reg-email" name="email" type="email" required maxlength="120"></div>
      <div class="field"><label for="reg-password">Contraseña</label><input id="reg-password" name="password" type="password" required minlength="6" maxlength="72"></div>
      <div class="field"><label for="register-avatar">Foto de perfil <span class="meta">(opcional)</span></label><input id="register-avatar" type="file" accept="image/png,image/jpeg,image/webp"></div>
      <div id="provider-fields" hidden>
        <div class="field"><label for="profession">Profesión</label><select id="profession" name="profession">${PROFESSIONS.map(p=>`<option value="${p}">${professionLabel(p)}</option>`).join("")}</select></div>
        <div class="field"><label for="phone">Teléfono</label><input id="phone" name="phone" maxlength="30"></div>
        <div class="field"><label for="description">Descripción</label><textarea id="description" name="description" maxlength="500"></textarea></div>
      </div>
      <p id="register-error" class="form-error" role="alert"></p>
      <button class="btn" type="submit">Crear cuenta</button>
    </form></div>`);
  const role=document.querySelector("#role"),providerFields=document.querySelector("#provider-fields");
  const toggle=()=>providerFields.hidden=role.value!=="PROVIDER";role.addEventListener("change",toggle);toggle();
  document.querySelector("#register-form").addEventListener("submit",async event=>{
    event.preventDefault();const d=new FormData(event.currentTarget);const email=String(d.get("email")).trim().toLowerCase();
    if(state.users.some(u=>u.email.toLowerCase()===email)){document.querySelector("#register-error").textContent="Ese correo electrónico ya está registrado.";return}
    const isProvider=d.get("role")==="PROVIDER";
    let image=isProvider?"./img/provider-avatar.png":"./img/customer-avatar.png";
    const file=document.querySelector("#register-avatar").files?.[0];
    try{if(file)image=await imageFileToAvatar(file)}catch(error){document.querySelector("#register-error").textContent=error.message;return}
    const user={id:uid("u"),name:String(d.get("name")).trim(),lastname:String(d.get("lastname")).trim(),email,password:String(d.get("password")),role:isProvider?"PROVIDER":"CUSTOMER",profession:isProvider?d.get("profession"):null,phone:isProvider?String(d.get("phone")).trim():"",description:isProvider?String(d.get("description")).trim():"",rating:0,active:true,image};
    state.users.push(user);state.currentUserId=user.id;persist();toast("Cuenta creada");go("home");
  });
}

function profile(id){
  const me=currentUser();const target=id==="me"?me:userById(id);
  if(!target){layout('<div class="empty">Perfil no encontrado.</div>');return}
  const isMe=me?.id===target.id;const canHire=me?.role==="CUSTOMER"&&target.role==="PROVIDER"&&me.id!==target.id;
  const count=target.role==="PROVIDER"?reviewCount(target.id):0;
  layout(`<section class="profile card">
    <img class="profile-image" src="${escapeHtml(target.image)}" alt="Foto de ${escapeHtml(target.name)} ${escapeHtml(target.lastname)}">
    <div><span class="badge">${target.role==="PROVIDER"?professionLabel(target.profession):ROLE_LABELS[target.role]}</span>
      <h1 class="page-title">${escapeHtml(target.name)} ${escapeHtml(target.lastname)}</h1>
      ${target.role==="PROVIDER"?`<div class="rating-line"><span class="rating">${stars(target.rating)}</span><span class="rating-number">${target.rating.toFixed?.(1)??target.rating}</span><span class="review-count">${count} ${count===1?"valoración":"valoraciones"}</span></div><p>${escapeHtml(target.description)}</p><div class="profile-meta"><span>${escapeHtml(target.phone)}</span><span>${escapeHtml(target.email)}</span></div>`:`<p class="meta">${escapeHtml(target.email)}</p>`}
      <div class="actions">
        ${canHire?`<button class="btn" data-action="hire" data-id="${target.id}">Solicitar trabajo</button>`:""}
        ${isMe?'<button class="btn secondary" data-action="edit-profile">Editar perfil</button>':""}
      </div>
    </div>
  </section>
  ${target.role==="PROVIDER"?reviewsFor(target.id):""}`);
}
function reviewsFor(providerId){
  const reviews=state.works.filter(w=>w.providerId===providerId&&w.status==="REVIEWD"&&w.review);
  return `<section class="section"><div class="section-header"><div><span class="eyebrow">Opiniones</span><h2 class="section-title">Valoraciones de clientes</h2></div></div><div class="grid">${reviews.length?reviews.map(w=>{const customer=userById(w.customerId);return `<article class="card review-card"><img class="review-avatar" src="${escapeHtml(customer?.image)}" alt=""><div><strong>${escapeHtml(customer?.name)} ${escapeHtml(customer?.lastname)}</strong><div class="rating-line"><span class="rating">${stars(w.rating)}</span><span class="rating-number">${w.rating}.0</span></div><blockquote>“${escapeHtml(w.review)}”</blockquote></div></article>`}).join(""):'<p class="empty">Aún no hay valoraciones.</p>'}</div></section>`;
}

function showHire(providerId){
  const p=userById(providerId);if(!requireRole("CUSTOMER")||!p)return;
  layout(`<div class="form-card card"><h1 class="page-title">Solicitar trabajo</h1><p>Profesional: <strong>${escapeHtml(p.name)} ${escapeHtml(p.lastname)}</strong></p>
  <form id="work-form"><div class="field"><label for="work-name">Trabajo</label><input id="work-name" name="name" required maxlength="100"></div>
  <div class="field"><label for="work-description">¿Qué necesitas?</label><textarea id="work-description" name="description" required maxlength="700"></textarea></div>
  <button class="btn" type="submit">Enviar solicitud</button></form></div>`);
  document.querySelector("#work-form").addEventListener("submit",event=>{event.preventDefault();const d=new FormData(event.currentTarget);state.works.push({id:uid("w"),customerId:currentUser().id,providerId:p.id,name:String(d.get("name")).trim(),description:String(d.get("description")).trim(),status:"REQUIRED",review:"",rating:0});persist();toast("Solicitud creada");go("works")});
}

function works(){
  if(!requireUser())return;const user=currentUser();
  const list=user.role==="ADMIN"?state.works:state.works.filter(w=>user.role==="CUSTOMER"?w.customerId===user.id:w.providerId===user.id);
  layout(`<h1 class="page-title">${user.role==="PROVIDER"?"Mis trabajos":user.role==="CUSTOMER"?"Mis solicitudes":"Trabajos"}</h1>
  <div class="work-list">${list.length?list.map(w=>workCard(w,user)).join(""):'<div class="empty card">Aún no hay trabajos.</div>'}</div>`);
}
function workCard(w,user){
  const customer=userById(w.customerId),provider=userById(w.providerId);
  let actions="";
  if(user.role==="PROVIDER"&&w.status==="REQUIRED") actions=`<button class="btn small" data-work="${w.id}" data-status="ACCEPTED">Aceptar</button><button class="btn small danger" data-work="${w.id}" data-status="REVERT">Cancelar</button>`;
  if((user.role==="CUSTOMER"||user.role==="PROVIDER")&&w.status==="ACCEPTED") actions=`<button class="btn small" data-work="${w.id}" data-status="DONE">Finalizar</button><button class="btn small danger" data-work="${w.id}" data-status="REVERT">Cancelar</button>`;
  if(user.role==="CUSTOMER"&&w.status==="REQUIRED") actions=`<button class="btn small danger" data-work="${w.id}" data-status="REVERT">Cancelar solicitud</button>`;
  if(user.role==="CUSTOMER"&&w.status==="DONE") actions=`<button class="btn small" data-review="${w.id}">Dejar valoración</button>`;
  return `<article class="card work-card"><div><span class="status ${w.status}">${STATUS_LABELS[w.status]}</span><h3>${escapeHtml(w.name)}</h3><p>${escapeHtml(w.description)}</p><div class="work-people"><span class="person-chip"><img src="${escapeHtml(customer?.image)}" alt="">Cliente: ${escapeHtml(customer?.name)} ${escapeHtml(customer?.lastname)}</span><span class="person-chip"><img src="${escapeHtml(provider?.image)}" alt="">Profesional: ${escapeHtml(provider?.name)} ${escapeHtml(provider?.lastname)}</span></div>${w.status==="REVIEWD"?`<div class="rating-line"><span class="rating">${stars(w.rating)}</span><span class="rating-number">${w.rating}.0</span></div><p>“${escapeHtml(w.review)}”</p>`:""}</div><div class="actions">${actions}</div></article>`;
}

function reviewForm(workId){
  if(!requireRole("CUSTOMER"))return;const w=state.works.find(x=>x.id===workId&&x.customerId===currentUser().id&&x.status==="DONE");if(!w){go("works");return}
  layout(`<div class="form-card card"><h1 class="page-title">Valorar trabajo</h1><form id="review-form">
  <div class="field"><label for="rating">Puntuación</label><select id="rating" name="rating">${[5,4,3,2,1].map(n=>`<option value="${n}">${n} estrellas</option>`).join("")}</select></div>
  <div class="field"><label for="review">Comentario</label><textarea id="review" name="review" required maxlength="500"></textarea></div>
  <button class="btn" type="submit">Publicar valoración</button></form></div>`);
  document.querySelector("#review-form").addEventListener("submit",event=>{event.preventDefault();const d=new FormData(event.currentTarget);w.rating=Number(d.get("rating"));w.review=String(d.get("review")).trim();w.status="REVIEWD";recalculateRating(w.providerId);persist();toast("Valoración publicada");go("works")});
}
function recalculateRating(providerId){const reviewed=state.works.filter(w=>w.providerId===providerId&&w.status==="REVIEWD"&&w.rating>0);const provider=userById(providerId);provider.rating=reviewed.length?Math.round(reviewed.reduce((a,w)=>a+w.rating,0)/reviewed.length):0}

function editProfile(){
  if(!requireUser())return;const u=currentUser();
  layout(`<div class="form-card card"><h1 class="page-title">Editar perfil</h1><form id="edit-form">
    <div class="avatar-editor">
      <img id="edit-avatar-preview" class="profile-image" src="${escapeHtml(u.image)}" alt="Foto de perfil actual">
      <div><label class="btn secondary" for="edit-avatar">📷 Cambiar foto</label><input id="edit-avatar" type="file" accept="image/png,image/jpeg,image/webp" hidden><p class="meta">Se recorta en formato cuadrado y queda guardada en esta demo.</p></div>
    </div>
    <div class="field"><label for="edit-name">Nombre</label><input id="edit-name" name="name" value="${escapeHtml(u.name)}" required></div>
    <div class="field"><label for="edit-lastname">Apellido</label><input id="edit-lastname" name="lastname" value="${escapeHtml(u.lastname)}" required></div>
    ${u.role==="PROVIDER"?`<div class="field"><label for="edit-phone">Teléfono</label><input id="edit-phone" name="phone" value="${escapeHtml(u.phone)}"></div><div class="field"><label for="edit-description">Descripción</label><textarea id="edit-description" name="description">${escapeHtml(u.description)}</textarea></div>`:""}
    <button class="btn">Guardar</button></form></div>`);

  document.querySelector("#edit-avatar").addEventListener("change",async event=>{
    const file=event.target.files?.[0];if(!file)return;
    try{
      u.image=await imageFileToAvatar(file);
      document.querySelector("#edit-avatar-preview").src=u.image;
      persist();toast("Foto de perfil actualizada");
    }catch(error){toast(error.message)}
  });

  document.querySelector("#edit-form").addEventListener("submit",event=>{
    event.preventDefault();const d=new FormData(event.currentTarget);
    u.name=String(d.get("name")).trim();u.lastname=String(d.get("lastname")).trim();
    if(u.role==="PROVIDER"){u.phone=String(d.get("phone")).trim();u.description=String(d.get("description")).trim()}
    persist();toast("Perfil actualizado");go("profile/me");
  });
}

function admin(){
  if(!requireRole("ADMIN"))return;
  const users=state.users.filter(u=>u.id!==currentUser().id);
  layout(`<div class="section-header"><div><span class="eyebrow">Administración</span><h1 class="page-title">Usuarios de la demo</h1><p class="section-copy">Activa, desactiva o cambia el tipo de cuenta para probar permisos y vistas.</p></div></div>
  <div class="table-wrap"><table><thead><tr><th>Usuario</th><th>Rol</th><th>Profesión</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>
  ${users.map(u=>`<tr><td><div class="table-user"><img src="${escapeHtml(u.image)}" alt=""><span><strong>${escapeHtml(u.name)} ${escapeHtml(u.lastname)}</strong><br><small>${escapeHtml(u.email)}</small></span></div></td><td>${ROLE_LABELS[u.role]}</td><td>${u.profession?professionLabel(u.profession):"—"}</td><td><span class="badge ${u.active?"success":"off"}">${u.active?"Activo":"Inactivo"}</span></td><td><button class="btn small secondary" data-toggle-user="${u.id}">${u.active?"Desactivar":"Activar"}</button> ${u.role==="PROVIDER"?`<button class="btn small secondary" data-toggle-role="${u.id}">Pasar a cliente</button>`:""}</td></tr>`).join("")}
  </tbody></table></div>
  <div class="actions"><button class="btn danger" data-action="reset">Restaurar datos demo</button></div>`);
}

function about(){
  layout(`<section class="about"><img src="./img/about.svg" alt="Ilustración del proyecto"><div>
    <span class="badge">Proyecto personal</span><h1 class="page-title">Home Repair</h1>
    <h2>Desarrollado por Federico Trucco</h2>
    <p>Plataforma para conectar personas que necesitan reparaciones del hogar con profesionales de distintos rubros. La aplicación original fue desarrollada con Java, Spring Boot, Spring Security, JPA, Thymeleaf y MySQL.</p>
    <p>Esta versión es una demo funcional preparada para GitHub Pages: permite recorrer los principales casos de uso sin requerir un servidor Java ni una base de datos externa.</p>
    <div class="actions"><a class="btn" href="https://github.com/truquinio/home-repair" target="_blank" rel="noopener">Ver código en GitHub</a></div>
  </div></section>`);
}

function route(){
  const raw=(location.hash||"#home").slice(1);const [name,id]=raw.split("/");
  switch(name){case"home":home();break;case"providers":providers();break;case"login":login();break;case"register":register();break;case"profile":profile(id);break;case"works":works();break;case"review":reviewForm(id);break;case"hire":showHire(id);break;case"admin":admin();break;case"about":about();break;default:home()}
}
document.addEventListener("click",event=>{
  const el=event.target.closest("[data-action],[data-profession],[data-work],[data-review],[data-toggle-user],[data-toggle-role]");
  if(!el)return;
  if(el.dataset.action==="logout"){state.currentUserId=null;persist();toast("Sesión cerrada");go("home")}
  if(el.dataset.action==="hire")go(`hire/${el.dataset.id}`);
  if(el.dataset.action==="edit-profile")editProfile();
  if(el.dataset.action==="reset"){state=store.reset();toast("Datos demo restaurados");go("home")}
  if(el.dataset.profession){go("providers");setTimeout(()=>{const filter=document.querySelector("#profession-filter");if(filter){filter.value=el.dataset.profession;filter.dispatchEvent(new Event("change"))}},0)}
  if(el.dataset.work){
    const w=state.works.find(x=>x.id===el.dataset.work);
    const next=el.dataset.status;
    if(w&&allowedWorkTransition(currentUser(),w,next)){w.status=next;persist();toast("Estado actualizado");works()}
    else if(w){toast("Ese cambio de estado no está permitido")}
  }
  if(el.dataset.review)go(`review/${el.dataset.review}`);
  if(el.dataset.toggleUser){const u=userById(el.dataset.toggleUser);if(u){u.active=!u.active;if(state.currentUserId===u.id)state.currentUserId=null;persist();admin()}}
  if(el.dataset.toggleRole){
    const u=userById(el.dataset.toggleRole);
    if(u?.role==="PROVIDER"){
      u.role="CUSTOMER";
      u.profession=null;
      u.description="";
      u.phone="";
      u.rating=0;
      if(String(u.image).startsWith("./img/provider-avatar")){
        u.image="./img/customer-avatar.png";
      }
      persist();
      admin();
    }
  }
});
window.addEventListener("hashchange",route);
route();
