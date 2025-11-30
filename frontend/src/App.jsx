import React, { useEffect, useState } from 'react';

// Single-file React app (Tailwind classes) to talk to the provided Spring API.
// Save this as App.jsx in a Vite / Create React App project that has Tailwind configured.

const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080';

export default function App() {
    const [username, setUsername] = useState('');
    const [roleHint, setRoleHint] = useState('NONE'); // purely UI hint — server enforces real role
    const [articles, setArticles] = useState([]);
    const [selectedId, setSelectedId] = useState(null);
    const [selectedArticle, setSelectedArticle] = useState(null);
    const [newContent, setNewContent] = useState('');
    const [editContent, setEditContent] = useState('');
    const [users, setUsers] = useState([]);
    const [statusMsg, setStatusMsg] = useState('');

    // Helper to show status and auto-clear
    function showStatus(msg) {
        setStatusMsg(msg);
        setTimeout(() => setStatusMsg(''), 4000);
    }

    useEffect(() => {
        fetchUsers();
    }, []);

    async function fetchUsers() {
        try {
            const res = await fetch(`${API_BASE}/userbd/all`);
            if (!res.ok) throw new Error('Erreur récupération users');
            const data = await res.json();
            setUsers(data);
        } catch (e) {
            console.error(e);
            showStatus('Impossible de récupérer les utilisateurs');
        }
    }

    async function fetchArticles() {
        try {
            // include username query param if provided so server returns role-based DTOs
            const url = username ? `${API_BASE}/articles/all?username=${encodeURIComponent(username)}` : `${API_BASE}/articles/all`;
            const res = await fetch(url);
            if (!res.ok) throw new Error('Erreur récupération articles');
            const data = await res.json();
            setArticles(data);
        } catch (e) {
            console.error(e);
            showStatus('Impossible de récupérer les articles');
        }
    }

    async function viewArticle(id) {
        try {
            const url = username ? `${API_BASE}/articles/one?id=${id}&username=${encodeURIComponent(username)}` : `${API_BASE}/articles/one?id=${id}`;
            const res = await fetch(url);
            const data = await res.json();
            setSelectedId(id);
            setSelectedArticle(data);
            // fill editor if moderator or publisher
            if (data.contenu) setEditContent(data.contenu);
        } catch (e) {
            console.error(e);
            showStatus('Impossible de charger l\'article');
        }
    }

    async function addArticle(e) {
        e.preventDefault();
        if (!username) return showStatus('Entrez un nom d\'utilisateur (username)');
        try {
            const form = new URLSearchParams();
            form.append('contenu', newContent);
            form.append('username', username);
            const res = await fetch(`${API_BASE}/articles/add?contenu=${encodeURIComponent(newContent)}&username=${encodeURIComponent(username)}`, {
                method: 'POST'
            });
            const text = await res.text();
            showStatus(text);
            setNewContent('');
            fetchArticles();
        } catch (e) {
            console.error(e);
            showStatus('Erreur lors de l\'ajout');
        }
    }

    async function updateArticle(e) {
        e.preventDefault();
        if (!selectedId) return;
        try {
            const res = await fetch(`${API_BASE}/articles/update?id=${selectedId}&contenu=${encodeURIComponent(editContent)}&username=${encodeURIComponent(username)}`, {
                method: 'PATCH'
            });
            const text = await res.text();
            showStatus(text);
            fetchArticles();
            viewArticle(selectedId);
        } catch (e) {
            console.error(e);
            showStatus('Erreur mise à jour');
        }
    }

    async function deleteArticle(id) {
        if (!confirm('Supprimer cet article ?')) return;
        try {
            const res = await fetch(`${API_BASE}/articles/delete?id=${id}&username=${encodeURIComponent(username)}`, { method: 'DELETE' });
            const text = await res.text();
            showStatus(text);
            setSelectedArticle(null);
            setSelectedId(null);
            fetchArticles();
        } catch (e) {
            console.error(e);
            showStatus('Erreur suppression');
        }
    }

    async function addOpinion(type, articleId) {
        try {
            const res = await fetch(`${API_BASE}/opinions/add?type=${encodeURIComponent(type)}&username=${encodeURIComponent(username)}&articleId=${encodeURIComponent(articleId)}`, { method: 'POST' });
            const text = await res.text();
            showStatus(text);
            // refresh article list/details
            fetchArticles();
            if (selectedId === articleId) viewArticle(articleId);
        } catch (e) {
            console.error(e);
            showStatus('Erreur like/dislike');
        }
    }

    async function createUser(e) {
        e.preventDefault();
        const name = prompt('Nom d\'utilisateur à créer (le role doit être configuré côté serveur si vous voulez tester publisher/moderator)');
        if (!name) return;
        try {
            const res = await fetch(`${API_BASE}/userbd/add?name=${encodeURIComponent(name)}`, { method: 'POST' });
            const text = await res.text();
            showStatus(text || 'Utilisateur créé');
            fetchUsers();
        } catch (e) {
            console.error(e);
            showStatus('Erreur création utilisateur');
        }
    }

    // initial fetch when username changes (to get role-specific view)
    useEffect(() => {
        fetchArticles();
    }, [username]);

    return (
        <div className="min-h-screen bg-gray-50 p-6 font-sans">
            <div className="max-w-6xl mx-auto">
                <header className="flex items-center justify-between mb-6">
                    <h1 className="text-2xl font-bold">Articles — Frontend</h1>
                    <div className="flex items-center gap-3">
                        <input
                            placeholder="username (ex: alice)"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="border rounded px-3 py-1"
                        />
                        <select value={roleHint} onChange={(e) => setRoleHint(e.target.value)} className="border rounded px-2 py-1">
                            <option>NONE</option>
                            <option>PUBLISHER</option>
                            <option>MODERATOR</option>
                        </select>
                        <button onClick={() => fetchArticles()} className="bg-blue-600 text-white px-3 py-1 rounded">Rafraîchir</button>
                    </div>
                </header>

                <main className="grid grid-cols-3 gap-6">
                    <section className="col-span-1 bg-white p-4 rounded shadow">
                        <div className="flex items-center justify-between mb-3">
                            <h2 className="font-semibold">Utilisateurs</h2>
                            <div className="flex gap-2">
                                <button onClick={fetchUsers} className="text-sm px-2 py-1 border rounded">Reload</button>
                                <button onClick={createUser} className="text-sm px-2 py-1 bg-green-600 text-white rounded">Créer</button>
                            </div>
                        </div>
                        <div className="space-y-2 max-h-64 overflow-auto">
                            {users.length === 0 && <div className="text-sm text-gray-500">Aucun utilisateur</div>}
                            {users.map((u, idx) => (
                                <div key={idx} className="flex items-center justify-between border-b py-2">
                                    <div>
                                        <div className="font-medium">{u.name}</div>
                                        <div className="text-xs text-gray-500">role: {u.role ?? 'NONE'}</div>
                                    </div>
                                    <button onClick={() => setUsername(u.name)} className="text-sm px-2 py-1 border rounded">Se connecter</button>
                                </div>
                            ))}
                        </div>
                    </section>

                    <section className="col-span-2 bg-white p-4 rounded shadow">
                        <div className="flex items-start gap-6">
                            <div className="w-2/3">
                                <h2 className="font-semibold mb-2">Liste des articles</h2>
                                <div className="space-y-4 max-h-[420px] overflow-auto">
                                    {articles.length === 0 && <div className="text-sm text-gray-500">Aucun article</div>}
                                    {articles.map((a, i) => (
                                        <article key={i} className="border rounded p-3">
                                            <div className="flex justify-between items-start">
                                                <div>
                                                    <div className="font-medium">{a.auteur}</div>
                                                    <div className="text-sm text-gray-600">{a.datePublication ?? ''}</div>
                                                </div>
                                                <div className="flex gap-2">
                                                    <button onClick={() => viewArticle(a.id ?? a.articleId ?? i)} className="text-sm px-2 py-1 border rounded">Voir</button>
                                                </div>
                                            </div>
                                            <p className="mt-2 text-sm">{a.contenu?.slice(0, 240) ?? ''}{a.contenu && a.contenu.length > 240 ? '…' : ''}</p>
                                        </article>
                                    ))}
                                </div>

                                <div className="mt-4">
                                    <h3 className="font-semibold">Ajouter un article</h3>
                                    <form onSubmit={addArticle} className="mt-2 space-y-2">
                                        <textarea value={newContent} onChange={(e) => setNewContent(e.target.value)} className="w-full border rounded p-2" rows={4} placeholder="Contenu..." />
                                        <div className="flex gap-2">
                                            <button type="submit" className="px-3 py-1 bg-indigo-600 text-white rounded">Ajouter</button>
                                            <button type="button" onClick={() => { setNewContent(''); }} className="px-3 py-1 border rounded">Clear</button>
                                        </div>
                                        <div className="text-xs text-gray-500">Note: le backend n'autorise l'ajout que pour les users avec role=PUBLISHER (config côté serveur)</div>
                                    </form>
                                </div>
                            </div>

                            <div className="w-1/3">
                                <h3 className="font-semibold mb-2">Détail</h3>
                                {!selectedArticle && <div className="text-sm text-gray-500">Sélectionnez un article pour voir les détails</div>}
                                {selectedArticle && (
                                    <div>
                                        <div className="border rounded p-3">
                                            <div className="flex justify-between items-center">
                                                <div>
                                                    <div className="font-medium">{selectedArticle.auteur}</div>
                                                    <div className="text-xs text-gray-600">{selectedArticle.datePublication ?? ''}</div>
                                                </div>
                                                <div className="flex gap-2">
                                                    <button onClick={() => deleteArticle(selectedId)} className="text-sm px-2 py-1 bg-red-600 text-white rounded">Supprimer</button>
                                                </div>
                                            </div>

                                            <p className="mt-3 whitespace-pre-wrap">{selectedArticle.contenu}</p>

                                            {/* moderator DTO may include likes/dislikes lists */}
                                            {selectedArticle.usersLiked && (
                                                <div className="mt-3 text-sm">
                                                    <div>Likes: {selectedArticle.totalLikes} — {selectedArticle.usersLiked.join(', ')}</div>
                                                    <div>Dislikes: {selectedArticle.totalDislikes} — {selectedArticle.usersDisliked.join(', ')}</div>
                                                </div>
                                            )}

                                            <div className="mt-3 flex gap-2">
                                                <button onClick={() => addOpinion('like', selectedId)} className="px-2 py-1 border rounded">Like</button>
                                                <button onClick={() => addOpinion('dislike', selectedId)} className="px-2 py-1 border rounded">Dislike</button>
                                            </div>

                                            <form onSubmit={updateArticle} className="mt-3">
                                                <label className="text-sm">Éditer (si autorisé):</label>
                                                <textarea value={editContent} onChange={(e) => setEditContent(e.target.value)} className="w-full border rounded p-2 mt-1" rows={3} />
                                                <div className="flex gap-2 mt-2">
                                                    <button className="px-3 py-1 bg-yellow-500 rounded" type="submit">Mettre à jour</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                )}

                            </div>
                        </div>
                    </section>
                </main>

                <footer className="mt-6 text-sm text-gray-600 flex justify-between items-center">
                    <div>{statusMsg || 'Prêt'}</div>
                    <div>Astuce: le select "role" est informatif — le rôle réel est défini côté serveur.</div>
                </footer>
            </div>
        </div>
    );
}
