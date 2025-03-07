/*  describe('Affichage des sessions', () => {
    beforeEach(() => {
      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
          { id: 1, name: 'Session 1', date: '2025-03-01', description: 'Yoga session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
          { id: 2, name: 'Session 2', date: '2025-03-02', description: 'Meditation session', teacher_id:1, users: [1], createdAt: '2025-01-01', updatedAt: null },
        ],
      }).as('getSessions');

        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
            id: 1,
            name: 'Admin User',
            admin: true,
            },
        }).as('getSessionInfo');

      //simuler le login
      cy.login('yoga@studio.com','test!1234');
  
      cy.visit('/sessions');
      //cy.wait('@getSessions');
    });
  
    it('Devrait afficher la liste des sessions disponibles', () => {
      cy.get('.list').should('exist');
      cy.get('.item').should('have.length', 2);
      cy.get('.item').first().should('contain', 'Session 1');
      cy.get('.item').last().should('contain', 'Session 2');
    });

    it('Devrait afficher le bouton "Create" pour un admin', () => {
        cy.get('button[routerLink="create"]').should('be.visible');
      });
  });
   

 */

  describe('ListComponent', () => {
    beforeEach(() => {
      cy.intercept('POST', '/api/session', {
        body: {
          name: 'yoga',
          date: '2024-02-12',
          description: 'yoga',
          teacher_id: 1,
          users:[1]
        },
      }).as('getSessions');
      cy.visit('/sessions'); // Update with the correct route
    });
  
    it('should display the list of sessions', () => {
      //cy.wait('@getSessions');
      cy.get('.item').should('have.length', 2); // Assuming 2 sessions in the fixture
    });
  });