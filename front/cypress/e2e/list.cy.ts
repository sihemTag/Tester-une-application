 describe('Affichage des sessions', () => {
    beforeEach(() => {
      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
          { id: 1, name: 'Session 1', date: '2025-03-01', description: 'Yoga session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
          { id: 2, name: 'Session 2', date: '2025-03-02', description: 'Meditation session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
        ],
      }).as('getSessions');

       /*  cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
            id: 1,
            name: 'Admin User',
            admin: true,
            },
        }).as('getSessionInfo'); */

      //simuler le login
      cy.login('yoga@studio.com','test!1234');
  
      cy.visit('/sessions');
      //cy.wait('@getSessions');
    });
  
    it('Devrait afficher la liste des sessions disponibles', () => {
      cy.get('.list').should('exist'); // Vérifie que la liste existe
      cy.get('.item').should('have.length', 2); // Vérifie qu'il y a 2 sessions affichées
      cy.get('.item').first().should('contain', 'Session 1'); // Vérifie le nom de la première session
      cy.get('.item').last().should('contain', 'Session 2'); // Vérifie le nom de la dernière session
    });

    it('Devrait afficher le bouton "Create" pour un admin', () => {
        cy.get('button[routerLink="create"]').should('be.visible');
      });
  });
   

