import {
    transition,
    trigger,
    query,
    style,
    animate,
    group,
    AnimationQueryMetadata,
    AnimationGroupMetadata,
    AnimationMetadata,
} from '@angular/animations';

const duration = 0.3;

const slidingAnimationSteps = (
    readingDirection: boolean,
    translateName: 'translateX' | 'translateY' = 'translateX'
): (AnimationQueryMetadata | AnimationGroupMetadata)[] => {
    return [
        query(
            ':enter, :leave',
            style({
                position: 'fixed',
                width: 'calc(100% - 48px)',
                maxWidth: 'calc(1600px)',
                height: 'calc(100% - 120px)',
            }),
            { optional: true }
        ),
        group([
            query(
                ':enter',
                [
                    style({
                        transform: `${translateName}(${
                            readingDirection ? 100 : -100
                        }%)`,
                    }),
                    animate(
                        `${duration}s ease-in-out`,
                        style({ opacity: 1, transform: `${translateName}(0%)` })
                    ),
                ],
                { optional: true }
            ),
            query(
                ':leave',
                [
                    style({ transform: `${translateName}(0%)` }),
                    animate(
                        `${duration}s ease-in-out`,
                        style({
                            opacity: 0,
                            transform: `${translateName}(${
                                readingDirection ? -100 : 100
                            }%)`,
                        })
                    ),
                ],
                { optional: true }
            ),
        ]),
    ];
};

const verticalSliding = (
    ltr: boolean
): (AnimationQueryMetadata | AnimationGroupMetadata)[] => {
    return slidingAnimationSteps(ltr);
};

const horizontalSliding = (
    topToBottom: boolean
): (AnimationQueryMetadata | AnimationGroupMetadata)[] => {
    return slidingAnimationSteps(topToBottom, 'translateY');
};

const leftToRightRoutes = [
    ['Login', 'Registration'],
    ['Dashboard', 'PlayGame'],
    ['Vocabulary'],
    ['UserSearch'],
    ['Settings'],
    // TODO: Complete
];

const generateTransitions = (): AnimationMetadata[] => {
    const animations: AnimationMetadata[] = [];

    leftToRightRoutes.forEach((sameLevelRoutes, ix) => {
        sameLevelRoutes.forEach((r, iy) => {
            leftToRightRoutes.slice(0, ix).forEach((prevRoutes) =>
                prevRoutes.forEach((prev) => {
                    animations.push(
                        transition(`${prev} => ${r}`, verticalSliding(true)),
                        transition(`${r} => ${prev}`, verticalSliding(false))
                    );
                })
            );

            sameLevelRoutes.slice(0, iy).forEach((prev) => {
                animations.push(
                    transition(`${prev} => ${r}`, horizontalSliding(true)),
                    transition(`${r} => ${prev}`, horizontalSliding(false))
                );
            });
        });
    });

    return animations;
};

export const slideAnimation = trigger('routeAnimations', generateTransitions());
