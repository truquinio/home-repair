const STORAGE_KEY="homeRepairDemo.v1";
const PROFESSIONS=["ALBAÑIL","CARPINTERO","CERRAJERO","ELECTRICISTA","GASISTA","HERRERO","LIMPIEZA","PLOMERO"];
const STATUS_LABELS={REQUIRED:"Solicitado",ACCEPTED:"Aceptado",DONE:"Finalizado",REVERT:"Cancelado",REVIEWD:"Valorado"};

const seed=()=>({
  currentUserId:null,
  users:[
    {id:"u-admin",name:"Federico",lastname:"Trucco",email:"admin@homerepair.demo",password:"demo123",role:"ADMIN",profession:null,phone:"",description:"Administrador de la demo Home Repair.",rating:0,active:true,image:"./img/admin.svg"},
    {id:"u-customer",name:"Lucía",lastname:"Gómez",email:"cliente@homerepair.demo",password:"demo123",role:"CUSTOMER",profession:null,phone:"",description:"",rating:0,active:true,image:"./img/customer-avatar.png"},
    {id:"u-p1",name:"Martín",lastname:"Pérez",email:"plomero@homerepair.demo",password:"demo123",role:"PROVIDER",profession:"PLOMERO",phone:"+54 351 555 0101",description:"Plomería domiciliaria, pérdidas, grifería e instalaciones.",rating:5,active:true,image:"./img/provider-avatar-lightblue.png"},
    {id:"u-p2",name:"Carla",lastname:"Ruiz",email:"electricista@homerepair.demo",password:"demo123",role:"PROVIDER",profession:"ELECTRICISTA",phone:"+54 351 555 0102",description:"Instalaciones eléctricas, tableros, iluminación y mantenimiento.",rating:4,active:true,image:"./img/provider-avatar-violet.png"},
    {id:"u-p3",name:"Diego",lastname:"López",email:"carpintero@homerepair.demo",password:"demo123",role:"PROVIDER",profession:"CARPINTERO",phone:"+54 351 555 0103",description:"Muebles a medida, reparación y restauración de madera.",rating:5,active:true,image:"./img/provider-avatar-green.png"}
  ],
  works:[
    {id:"w-1",customerId:"u-customer",providerId:"u-p1",name:"Pérdida en cocina",description:"Pierde agua debajo de la bacha.",status:"DONE",review:"",rating:0},
    {id:"w-2",customerId:"u-customer",providerId:"u-p2",name:"Cambio de luminaria",description:"Reemplazar una lámpara de techo.",status:"ACCEPTED",review:"",rating:0}
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
const uid=prefix=>`${prefix}-${Date.now().toString(36)}-${Math.random().toString(36).slice(2,7)}`;
const persist=()=>store.save(state);
const go=route=>{location.hash=route};
const toast=message=>{toastEl.textContent=message;toastEl.classList.add("show");setTimeout(()=>toastEl.classList.remove("show"),2400)};

function nav(){
  const user=currentUser();
  header.innerHTML=`<nav class="nav" aria-label="Navegación principal"><div class="nav-inner">
    <a class="brand" href="#home">Home Repair</a>
    <div class="nav-links">
      <a href="#providers">Profesionales</a>
      ${user?'<a href="#works">Trabajos</a>':""}
      ${user?.role==="ADMIN"?'<a href="#admin">Admin</a>':""}
      <a href="#about">About</a>
      ${user?'<a href="#profile/me">Mi perfil</a><button class="link-button" data-action="logout">Salir</button>':'<a href="#login">Ingresar</a><a href="#register">Registrarse</a>'}
    </div></div></nav>`;
}

function layout(html){nav();app.innerHTML=`<div class="container">${html}</div>`;app.focus({preventScroll:true})}
function requireUser(){if(!currentUser()){go("login");return false}return true}
function requireRole(role){const user=currentUser();if(!user||user.role!==role){go("home");return false}return true}

function home(){
  const user=currentUser();
  layout(`<section class="hero">
    <div><span class="badge">Demo funcional</span>
      <h1>Home Repair</h1>
      <p>${user?`Hola ${escapeHtml(user.name)}. Probá los flujos de Home Repair con datos persistentes en tu navegador.`:"Reparaciones hogareñas. Encontrá profesionales, solicitá trabajos y calificá el servicio."}</p>
      <div class="actions">
        <a class="btn" href="#providers">🧰 Buscar servicios</a>
        ${user?'<a class="btn secondary" href="#works">Ver mis trabajos</a>':'<a class="btn secondary" href="#register">📝 Registrarme</a>'}
      </div>
    </div>
    <img class="hero-art" src="./img/homeRepair.svg" alt="Ilustración de reparaciones del hogar">
  </section>
  <section class="section"><h2 class="section-title">Servicios</h2>
    <div class="grid">${PROFESSIONS.map(p=>`<button class="card" data-profession="${p}"><h3>${titleCase(p)}</h3><p>Ver profesionales disponibles</p></button>`).join("")}</div>
  </section>
  <div class="notice"><strong>Sobre esta demo:</strong> las cuentas, roles y datos se simulan localmente para poder probar el producto en GitHub Pages. No representa autenticación de producción.</div>`);
}

function providers(){
  const active=state.users.filter(u=>u.role==="PROVIDER"&&u.active);
  layout(`<h1 class="page-title">Encontrá profesionales</h1>
  <p class="meta">Buscá por profesión, nombre o email.</p>
  <div class="filters">
    <select id="profession-filter" aria-label="Filtrar por profesión"><option value="">Todas las profesiones</option>${PROFESSIONS.map(p=>`<option value="${p}">${titleCase(p)}</option>`).join("")}</select>
    <input id="provider-search" type="search" placeholder="Nombre, apellido o email" aria-label="Buscar profesional">
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
function providerCard(u){return `<article class="card provider-card">
  <img class="card-avatar" src="${escapeHtml(u.image)}" alt="">
  <span class="badge">${titleCase(u.profession)}</span>
  <h3>${escapeHtml(u.name)} ${escapeHtml(u.lastname)}</h3>
  <div class="rating" aria-label="${u.rating} de 5 estrellas">${stars(u.rating)}</div>
  <p>${escapeHtml(u.description)}</p>
  <a class="btn small" href="#profile/${u.id}">Ver perfil</a>
</article>`}

function login(){
  layout(`<div class="form-card card"><h1 class="page-title">Ingresar</h1>
    <p>Podés usar una cuenta demo o tus datos creados dentro de este navegador.</p>
    <div class="demo-users">
      <button class="demo-user" data-demo="u-customer"><strong>Cliente</strong><span>cliente@homerepair.demo</span></button>
      <button class="demo-user" data-demo="u-p1"><strong>Proveedor</strong><span>plomero@homerepair.demo</span></button>
      <button class="demo-user" data-demo="u-admin"><strong>Admin</strong><span>admin@homerepair.demo</span></button>
    </div>
    <form id="login-form">
      <div class="field"><label for="email">Email</label><input id="email" name="email" type="email" autocomplete="email" required></div>
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
    if(!user){document.querySelector("#login-error").textContent="Email o contraseña incorrectos.";return}
    state.currentUserId=user.id;persist();go("home");
  });
}

function register(){
  layout(`<div class="form-card card"><h1 class="page-title">Registrate</h1>
    <form id="register-form">
      <div class="field"><label for="role">Quiero</label><select id="role" name="role"><option value="CUSTOMER">Contratar servicios</option><option value="PROVIDER">Ofrecer servicios</option></select></div>
      <div class="field"><label for="name">Nombre</label><input id="name" name="name" required maxlength="60"></div>
      <div class="field"><label for="lastname">Apellido</label><input id="lastname" name="lastname" required maxlength="60"></div>
      <div class="field"><label for="reg-email">Email</label><input id="reg-email" name="email" type="email" required maxlength="120"></div>
      <div class="field"><label for="reg-password">Contraseña</label><input id="reg-password" name="password" type="password" required minlength="6"></div>
      <div id="provider-fields" hidden>
        <div class="field"><label for="profession">Profesión</label><select id="profession" name="profession">${PROFESSIONS.map(p=>`<option value="${p}">${titleCase(p)}</option>`).join("")}</select></div>
        <div class="field"><label for="phone">Teléfono</label><input id="phone" name="phone" maxlength="30"></div>
        <div class="field"><label for="description">Descripción</label><textarea id="description" name="description" maxlength="500"></textarea></div>
      </div>
      <p id="register-error" class="form-error" role="alert"></p>
      <button class="btn" type="submit">Crear cuenta</button>
    </form></div>`);
  const role=document.querySelector("#role"),providerFields=document.querySelector("#provider-fields");
  const toggle=()=>providerFields.hidden=role.value!=="PROVIDER";role.addEventListener("change",toggle);toggle();
  document.querySelector("#register-form").addEventListener("submit",event=>{
    event.preventDefault();const d=new FormData(event.currentTarget);const email=String(d.get("email")).trim().toLowerCase();
    if(state.users.some(u=>u.email.toLowerCase()===email)){document.querySelector("#register-error").textContent="Ese email ya está registrado.";return}
    const isProvider=d.get("role")==="PROVIDER";const user={id:uid("u"),name:String(d.get("name")).trim(),lastname:String(d.get("lastname")).trim(),email,password:String(d.get("password")),role:isProvider?"PROVIDER":"CUSTOMER",profession:isProvider?d.get("profession"):null,phone:isProvider?String(d.get("phone")).trim():"",description:isProvider?String(d.get("description")).trim():"",rating:0,active:true,image:isProvider?"./img/provider-avatar.png":"./img/customer-avatar.png"};
    state.users.push(user);state.currentUserId=user.id;persist();toast("Cuenta creada");go("home");
  });
}

function profile(id){
  const me=currentUser();const target=id==="me"?me:userById(id);
  if(!target){layout('<div class="empty">Perfil no encontrado.</div>');return}
  const isMe=me?.id===target.id;const canHire=me?.role==="CUSTOMER"&&target.role==="PROVIDER"&&me.id!==target.id;
  layout(`<section class="profile card">
    <img class="profile-image" src="${escapeHtml(target.image)}" alt="">
    <div><span class="badge">${target.role==="PROVIDER"?titleCase(target.profession):target.role}</span>
      <h1 class="page-title">${escapeHtml(target.name)} ${escapeHtml(target.lastname)}</h1>
      ${target.role==="PROVIDER"?`<p class="rating">${stars(target.rating)} · ${target.rating}/5</p><p>${escapeHtml(target.description)}</p><p class="meta">${escapeHtml(target.phone)}</p>`:""}
      <p class="meta">${escapeHtml(target.email)}</p>
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
  return `<section class="section"><h2 class="section-title">Valoraciones</h2><div class="grid">${reviews.length?reviews.map(w=>`<article class="card"><p>${stars(w.rating)}</p><p>“${escapeHtml(w.review)}”</p><small>${escapeHtml(userById(w.customerId)?.name)}</small></article>`).join(""):'<p class="empty">Todavía no hay valoraciones.</p>'}</div></section>`;
}

function showHire(providerId){
  const p=userById(providerId);if(!requireRole("CUSTOMER")||!p)return;
  layout(`<div class="form-card card"><h1 class="page-title">Solicitar trabajo</h1><p>Profesional: <strong>${escapeHtml(p.name)} ${escapeHtml(p.lastname)}</strong></p>
  <form id="work-form"><div class="field"><label for="work-name">Trabajo</label><input id="work-name" name="name" required maxlength="100"></div>
  <div class="field"><label for="work-description">¿Qué necesitás?</label><textarea id="work-description" name="description" required maxlength="700"></textarea></div>
  <button class="btn" type="submit">Enviar solicitud</button></form></div>`);
  document.querySelector("#work-form").addEventListener("submit",event=>{event.preventDefault();const d=new FormData(event.currentTarget);state.works.push({id:uid("w"),customerId:currentUser().id,providerId:p.id,name:String(d.get("name")).trim(),description:String(d.get("description")).trim(),status:"REQUIRED",review:"",rating:0});persist();toast("Solicitud creada");go("works")});
}

function works(){
  if(!requireUser())return;const user=currentUser();
  const list=user.role==="ADMIN"?state.works:state.works.filter(w=>user.role==="CUSTOMER"?w.customerId===user.id:w.providerId===user.id);
  layout(`<h1 class="page-title">${user.role==="PROVIDER"?"Mis trabajos":user.role==="CUSTOMER"?"Mis solicitudes":"Trabajos"}</h1>
  <div class="work-list">${list.length?list.map(w=>workCard(w,user)).join(""):'<div class="empty card">Todavía no hay trabajos.</div>'}</div>`);
}
function workCard(w,user){
  const customer=userById(w.customerId),provider=userById(w.providerId);
  let actions="";
  if(user.role==="PROVIDER"&&w.status==="REQUIRED") actions=`<button class="btn small" data-work="${w.id}" data-status="ACCEPTED">Aceptar</button><button class="btn small danger" data-work="${w.id}" data-status="REVERT">Cancelar</button>`;
  if((user.role==="CUSTOMER"||user.role==="PROVIDER")&&w.status==="ACCEPTED") actions=`<button class="btn small" data-work="${w.id}" data-status="DONE">Finalizar</button><button class="btn small danger" data-work="${w.id}" data-status="REVERT">Cancelar</button>`;
  if(user.role==="CUSTOMER"&&w.status==="REQUIRED") actions=`<button class="btn small danger" data-work="${w.id}" data-status="REVERT">Cancelar solicitud</button>`;
  if(user.role==="CUSTOMER"&&w.status==="DONE") actions=`<button class="btn small" data-review="${w.id}">Dejar valoración</button>`;
  return `<article class="card work-card"><div><span class="status ${w.status}">${STATUS_LABELS[w.status]}</span><h3>${escapeHtml(w.name)}</h3><p>${escapeHtml(w.description)}</p><p class="meta">Cliente: ${escapeHtml(customer?.name)} · Profesional: ${escapeHtml(provider?.name)} ${escapeHtml(provider?.lastname)}</p>${w.status==="REVIEWD"?`<p>${stars(w.rating)} — “${escapeHtml(w.review)}”</p>`:""}</div><div class="actions">${actions}</div></article>`;
}

function reviewForm(workId){
  if(!requireRole("CUSTOMER"))return;const w=state.works.find(x=>x.id===workId&&x.customerId===currentUser().id&&x.status==="DONE");if(!w){go("works");return}
  layout(`<div class="form-card card"><h1 class="page-title">Valorar trabajo</h1><form id="review-form">
  <div class="field"><label for="rating">Puntuación</label><select id="rating" name="rating">${[5,4,3,2,1].map(n=>`<option value="${n}">${n} estrellas</option>`).join("")}</select></div>
  <div class="field"><label for="review">Comentario</label><textarea id="review" name="review" required maxlength="500"></textarea></div>
  <button class="btn">Publicar valoración</button></form></div>`);
  document.querySelector("#review-form").addEventListener("submit",event=>{event.preventDefault();const d=new FormData(event.currentTarget);w.rating=Number(d.get("rating"));w.review=String(d.get("review")).trim();w.status="REVIEWD";recalculateRating(w.providerId);persist();toast("Valoración publicada");go("works")});
}
function recalculateRating(providerId){const reviewed=state.works.filter(w=>w.providerId===providerId&&w.status==="REVIEWD"&&w.rating>0);const provider=userById(providerId);provider.rating=reviewed.length?Math.round(reviewed.reduce((a,w)=>a+w.rating,0)/reviewed.length):0}

function editProfile(){
  if(!requireUser())return;const u=currentUser();
  layout(`<div class="form-card card"><h1 class="page-title">Editar perfil</h1><form id="edit-form">
    <div class="field"><label for="edit-name">Nombre</label><input id="edit-name" name="name" value="${escapeHtml(u.name)}" required></div>
    <div class="field"><label for="edit-lastname">Apellido</label><input id="edit-lastname" name="lastname" value="${escapeHtml(u.lastname)}" required></div>
    ${u.role==="PROVIDER"?`<div class="field"><label for="edit-phone">Teléfono</label><input id="edit-phone" name="phone" value="${escapeHtml(u.phone)}"></div><div class="field"><label for="edit-description">Descripción</label><textarea id="edit-description" name="description">${escapeHtml(u.description)}</textarea></div>`:""}
    <button class="btn">Guardar</button></form></div>`);
  document.querySelector("#edit-form").addEventListener("submit",event=>{event.preventDefault();const d=new FormData(event.currentTarget);u.name=String(d.get("name")).trim();u.lastname=String(d.get("lastname")).trim();if(u.role==="PROVIDER"){u.phone=String(d.get("phone")).trim();u.description=String(d.get("description")).trim()}persist();toast("Perfil actualizado");go("profile/me")});
}

function admin(){
  if(!requireRole("ADMIN"))return;
  const users=state.users.filter(u=>u.id!==currentUser().id);
  layout(`<h1 class="page-title">Administración</h1><p class="meta">Gestión simulada de usuarios de la demo.</p>
  <div class="table-wrap"><table><thead><tr><th>Usuario</th><th>Rol</th><th>Profesión</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>
  ${users.map(u=>`<tr><td>${escapeHtml(u.name)} ${escapeHtml(u.lastname)}<br><small>${escapeHtml(u.email)}</small></td><td>${u.role}</td><td>${titleCase(u.profession||"—")}</td><td><span class="badge ${u.active?"success":"off"}">${u.active?"Activo":"Inactivo"}</span></td><td><button class="btn small secondary" data-toggle-user="${u.id}">${u.active?"Desactivar":"Activar"}</button> ${u.role!=="ADMIN"?`<button class="btn small secondary" data-toggle-role="${u.id}">Cambiar rol</button>`:""}</td></tr>`).join("")}
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
  if(el.dataset.work){const w=state.works.find(x=>x.id===el.dataset.work);if(w){w.status=el.dataset.status;persist();toast("Estado actualizado");works()}}
  if(el.dataset.review)go(`review/${el.dataset.review}`);
  if(el.dataset.toggleUser){const u=userById(el.dataset.toggleUser);if(u){u.active=!u.active;if(state.currentUserId===u.id)state.currentUserId=null;persist();admin()}}
  if(el.dataset.toggleRole){const u=userById(el.dataset.toggleRole);if(u){u.role=u.role==="CUSTOMER"?"PROVIDER":"CUSTOMER";if(u.role==="PROVIDER"){u.profession=u.profession||"PLOMERO";u.image="./img/provider-avatar.png"}else{u.profession=null;u.description="";u.phone="";u.image="./img/customer-avatar.png"}persist();admin()}}
});
window.addEventListener("hashchange",route);
route();
